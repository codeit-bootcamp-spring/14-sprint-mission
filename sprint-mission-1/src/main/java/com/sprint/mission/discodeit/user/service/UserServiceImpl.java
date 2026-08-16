package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ExceptionType;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponseDto userCreate(UserCreateRequestDto userCreateRequestDto) {
        if (userRepository.findByUserName(userCreateRequestDto.name()).isPresent()) {
            throw new DiscodeitException(
                ExceptionType.USER_NAME_CONFLICT,
                Map.of("userName", userCreateRequestDto.name())
            );
        }

        if (userRepository.findByUserEmail(userCreateRequestDto.email()).isPresent()) {
            throw new DiscodeitException(
                ExceptionType.USER_EMAIL_CONFLICT,
                Map.of("userEmail", userCreateRequestDto.email())
            );
        }

        UUID binaryContentsId = null;
        if (userCreateRequestDto.profileImage() != null) {
            binaryContentsId = binaryContentRepository.toBinaryContent(
                userCreateRequestDto.profileImage()).getBinaryContentId();
        }

        User user = User.create(userCreateRequestDto.name(), userCreateRequestDto.password(),
            userCreateRequestDto.email(),
            binaryContentsId);

        UserStatus userStatus = userStatusRepository.statusAdd(new UserStatus(user.getId()));

        return UserResponseDto.from(userRepository.userAdd(user), userStatus);
    }

    @Override
    public UserResponseDto userUpdate(UUID userId, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findByUser(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_NOT_FOUND,
                Map.of("userId", userId)
            ));

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
                throw new UncheckedIOException(
                    "파일을 읽는데 실패했습니다: " + userUpdateRequestDto.profileImage().getOriginalFilename(),
                    e);
            }
            user.updateProfile(binaryContent.getBinaryContentId());
        }

        user.update(userUpdateRequestDto.name(), userUpdateRequestDto.password(),
            userUpdateRequestDto.email());

        userRepository.update(user);

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_STATUS_MISSING_FOR_USER,
                Map.of("userId", user.getId()
                )));
        return UserResponseDto.from(user, userStatus);
    }

    @Override
    public void userDelete(UUID userId) {
        User user = userRepository.findByUser(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_NOT_FOUND,
                Map.of("userId", userId)
            ));

        userStatusRepository.delete(userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_STATUS_MISSING_FOR_USER,
                Map.of("userId", user.getId()
                ))));
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
                UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new DiscodeitException(
                        ExceptionType.USER_STATUS_MISSING_FOR_USER,
                        Map.of("userId", user.getId()
                        )));
                return UserResponseDto.from(user, userStatus);
            })
            .toList();
    }

    @Override
    public UserResponseDto findById(UUID userId) {
        User user = userRepository.findByUser(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_NOT_FOUND,
                Map.of("userId", userId)
            ));

        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_STATUS_MISSING_FOR_USER,
                Map.of("userId", user.getId()
                )));

        return UserResponseDto.from(user, userStatus);
    }
}
