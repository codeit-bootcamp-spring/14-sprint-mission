package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.NoSuchElementException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public UserResponseDto create(UserRequestDto userRequestDto) {

        //findByUsername, findByEmail 구현하기
        if (this.findByUsername(userRequestDto.userName()).isPresent()) {
            throw new DuplicateUsernameException();
        }
        if (this.findByEmail(userRequestDto.email()).isPresent()) {
            throw new DuplicateEmailException();
        }

        User user = new User(userRequestDto);

        // 사진이 있으면
        BinaryContent binaryContent;
        if (userRequestDto.data() != null) {
            binaryContent = new BinaryContent(userRequestDto.data());
            binaryContentRepository.save(binaryContent);
            user.updateProfileId(binaryContent.getId());
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
    public void update(UserUpdateRequestDto userUpdateRequestDto) {
        User user = userCheck(userUpdateRequestDto.userId());

        // 사진이 있으면
        if (userUpdateRequestDto.data() != null) {
            BinaryContent binaryContent = new BinaryContent(userUpdateRequestDto.data());

            //기존에 파일이 있었으면
            if (user.getProfileId() != null) {
                binaryContentRepository.deleteById(user.getProfileId());    // 사진을 지워라
            }

            binaryContentRepository.save(binaryContent);
            user.updateProfileId(binaryContent.getId());
        }

        user.update(userUpdateRequestDto.newUsername(),userUpdateRequestDto.newEmail(), userUpdateRequestDto.newPassword());
        userRepository.update(user);
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
