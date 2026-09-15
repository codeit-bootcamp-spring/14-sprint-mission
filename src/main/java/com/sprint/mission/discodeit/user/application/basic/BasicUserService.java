package com.sprint.mission.discodeit.user.application.basic;

import com.sprint.mission.discodeit.binaryContent.storage.BinaryContentStorage;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
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
            binaryContent = new BinaryContent(profile.getOriginalFilename(),
                    profile.getSize(),
                    profile.getContentType());
            binaryContentRepository.save(binaryContent);
            try {
                binaryContentStorage.put(binaryContent.getId(), profile.getBytes());
            } catch (IOException e){
                throw new UncheckedIOException(e);
            }
            user.updateProfile(binaryContent);

        }
        UserStatus userStatus = UserStatus.create(user);
        user.updateUserStatus(userStatus);

        userRepository.save(user);  // 영속성 전이로 자식까지 넣음


        return UserResponseDto.from(user, userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto find(UUID id) {
        User user = userCheck(id);
        UserStatus userStatus = userStatusRepository.findByUserId(id).orElseThrow();
        return UserResponseDto.from(user, userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {

        return userRepository.findAll().stream()
                .map(user -> find(user.getId()))
                .toList();
    }

    @Override
    @Transactional
    public UserResponseDto update(UUID id, UserUpdateRequestDto userUpdateRequestDto, MultipartFile profile) {
        User user = userCheck(id);
        UserStatus userStatus = userStatusRepository.findByUserId(id).orElseThrow();
        // 사진이 있으면
        if (profile != null) {

            BinaryContent binaryContent = new BinaryContent(profile.getOriginalFilename(),
                    profile.getSize(),
                    profile.getContentType());

            user.updateProfile(binaryContent);
//                binaryContentRepository.save(binaryContent);


        }

        user.update(userUpdateRequestDto.newUsername(), userUpdateRequestDto.newEmail(), userUpdateRequestDto.newPassword());

        return UserResponseDto.from(user, userStatus);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        User user = userCheck(id);
        // 프로필 삭제
//        if(user.getProfile() != null) {
//            binaryContentRepository.deleteById(user.getProfile().getId());
//        }
        // 유저 상태 삭제
//        userStatusRepository.deleteByUserId(user.getId());
        // 유저 삭제
        userRepository.deleteById(id);
    }


    private User userCheck(UUID id) {

        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
