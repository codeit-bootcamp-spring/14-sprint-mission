package com.sprint.mission.discodeit.user.application.basic;

import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.common.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.common.exception.DuplicateUsernameException;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import com.sprint.mission.discodeit.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponseDto create(UserCreateRequestDto userRequestDto, MultipartFile profile) {

        //findByUsername, findByEmail 구현하기
        if (this.findByUsername(userRequestDto.username()).isPresent()) {
            throw new DuplicateUsernameException();
        }
        if (this.findByEmail(userRequestDto.email()).isPresent()) {
            throw new DuplicateEmailException();
        }

        User user = User.create(userRequestDto.username(), userRequestDto.email(), userRequestDto.password());

        // 사진이 있으면
        BinaryContent binaryContent;
        if (profile != null) {
            try {
                binaryContent = new BinaryContent(profile.getOriginalFilename(),
                        profile.getSize(),
                        profile.getContentType(),
                        profile.getBytes());
                binaryContentRepository.save(binaryContent);
                user.updateProfileId(binaryContent.getId());
            } catch (IOException e) {
                throw new NoSuchElementException();
            }
        }

        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);


        return UserResponseDto.from(user, userStatus);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserResponseDto find(UUID id) {
        User user = userCheck(id);
        UserStatus userStatus = userStatusRepository.findByUserId(id).orElseThrow();
        return UserResponseDto.from(user, userStatus);
    }

    @Override
    public List<UserResponseDto> findAll() {

        return userRepository.findAll().stream()
                .map(user -> find(user.getId()))
                .toList();
    }

    @Override
    public UserResponseDto update(UUID id, UserUpdateRequestDto userUpdateRequestDto, MultipartFile profile) {
        User user = userCheck(id);
        UserStatus userStatus = userStatusRepository.findByUserId(id).orElseThrow();
        // 사진이 있으면
        if (profile != null) {
            try {
                BinaryContent binaryContent = new BinaryContent(profile.getOriginalFilename(),
                        profile.getSize(),
                        profile.getContentType(),
                        profile.getBytes());

                //기존에 파일이 있었으면
                if (user.getProfileId() != null) {
                    binaryContentRepository.deleteById(user.getProfileId());    // 사진을 지워라
                }

                binaryContentRepository.save(binaryContent);
                user.updateProfileId(binaryContent.getId());
            } catch (IOException e){
                throw new NoSuchElementException();
            }

        }

        user.update(userUpdateRequestDto.newUsername(), userUpdateRequestDto.newEmail(), userUpdateRequestDto.newPassword());
        userRepository.update(user);

        return UserResponseDto.from(user, userStatus);
    }

    @Override
    public void delete(UUID id) {
        User user = userCheck(id);
        // 프로필 삭제
        binaryContentRepository.deleteById(user.getProfileId());
        // 유저 상태 삭제
        userStatusRepository.deleteByUserId(user.getId());
        // 유저 삭제
        userRepository.deleteById(id);
    }


    private User userCheck(UUID id) {

        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
