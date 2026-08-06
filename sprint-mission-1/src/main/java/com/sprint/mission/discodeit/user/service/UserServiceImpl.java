package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    public UserServiceImpl(UserRepository userRepository,
        BinaryContentRepository binaryContentRepository,
        UserStatusRepository userStatusRepository) {
        this.userRepository = userRepository;
        this.binaryContentRepository = binaryContentRepository;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public UserResponseDto userCreate(UserCreateRequestDto userCreateRequestDto) {
        if (userRepository.findByUserName(userCreateRequestDto.name()).isPresent()) {
            throw new IllegalArgumentException(
                "이미 존재하는 유저 이름입니다: " + userCreateRequestDto.name());
        }

        if (userRepository.findByUserEmail(userCreateRequestDto.email()).isPresent()) {
            throw new IllegalArgumentException(
                "이미 존재하는 이메일입니다: " + userCreateRequestDto.email());
        }

        UUID binaryContentsId = null;
        if (userCreateRequestDto.profileImage() != null) {
            binaryContentsId = binaryContentRepository.toBinaryContent(
                userCreateRequestDto.profileImage()).getBinaryContentId();
        }

        User user = new User(userCreateRequestDto.name(), userCreateRequestDto.password(),
            userCreateRequestDto.email(),
            binaryContentsId);

        UserStatus userStatus = userStatusRepository.statusAdd(new UserStatus(user.getUserId()));

        return UserResponseDto.from(userRepository.userAdd(user), userStatus);
    }

    @Override
    public UserResponseDto userUpdate(UUID userId, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findByUser(userId)
            .orElseThrow(() -> new IllegalArgumentException("수정할 유저가 없습니다: " + userId));

        if (userUpdateRequestDto.name() != null) {
            user.updateName(userUpdateRequestDto.name());
        }
        if (userUpdateRequestDto.email() != null) {
            user.updateEmail(userUpdateRequestDto.email());
        }
        if (userUpdateRequestDto.password() != null) {
            user.updatePassword(userUpdateRequestDto.password());
        }

        if (userUpdateRequestDto.profileImage() != null) {
            binaryContentRepository.delete(user.getBinaryId());
            BinaryContent binaryContent;
            try {
                binaryContent = new BinaryContent(
                    userUpdateRequestDto.profileImage().getOriginalFilename(),
                    userUpdateRequestDto.profileImage().getContentType(),
                    userUpdateRequestDto.profileImage().getBytes());
                binaryContentRepository.binaryAdd(binaryContent);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(
                    "파일을 읽는데 실패했습니다: " + userUpdateRequestDto.profileImage().getOriginalFilename());
            }
            user.updateBinaryId(binaryContent.getBinaryContentId());
        }

        userRepository.update(user);

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저 상태가 없습니다: " + userId));
        return UserResponseDto.from(user, userStatus);
    }

    @Override
    public void userDelete(UUID userId) {
        User user = userRepository.findByUser(userId)
            .orElseThrow(() -> new IllegalArgumentException("삭제할 유저가 없습니다: " + userId));

        userStatusRepository.delete(userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저 상태가 없습니다: " + userId)));
        if (!Objects.isNull(user.getBinaryId())) {
            binaryContentRepository.delete(user.getBinaryId());
        }
        userRepository.delete(user);
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAllUser();

        return users.stream()
            .map(user -> {
                UserStatus userStatus = userStatusRepository.findByUserId(user.getUserId())
                    .orElseThrow(
                        () -> new IllegalArgumentException("유저 상태가 없습니다: " + user.getUserId()));
                return UserResponseDto.from(user, userStatus);
            })
            .toList();
    }

    @Override
    public UserResponseDto findById(UUID userId) {
        User user = userRepository.findByUser(userId)
            .orElseThrow(() -> new IllegalArgumentException("보고자 하는 유저가 없습니다: " + userId));

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저 상태가 없습니다: " + userId));

        return UserResponseDto.from(user, userStatus);
    }
}
