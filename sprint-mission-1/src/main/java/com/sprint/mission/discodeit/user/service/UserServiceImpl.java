package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ExceptionType;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
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
    public UserDto userCreate(UserCreateRequestDto userCreateRequestDto) {
        if (userRepository.findByUserName(userCreateRequestDto.username()).isPresent()) {
            throw new DiscodeitException(
                ExceptionType.USER_NAME_CONFLICT,
                Map.of("userName", userCreateRequestDto.username())
            );
        }

        if (userRepository.findByUserEmail(userCreateRequestDto.email()).isPresent()) {
            throw new DiscodeitException(
                ExceptionType.USER_EMAIL_CONFLICT,
                Map.of("userEmail", userCreateRequestDto.email())
            );
        }

//        UUID binaryContentsId = null;
//        if (userCreateRequestDto.profileImage() != null) {
//            binaryContentsId = binaryContentRepository.toBinaryContent(
//                userCreateRequestDto.profileImage()).getId();
//        }

        User user = User.create(userCreateRequestDto.username(), userCreateRequestDto.password(),
            userCreateRequestDto.email());

        UserStatus userStatus = userStatusRepository.statusAdd(new UserStatus(user.getId()));

        return UserDto.from(userRepository.userAdd(user), userStatus);
    }

    @Override
    public UserDto userUpdate(UUID userId, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findByUser(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_NOT_FOUND,
                Map.of("authorId", userId)
            ));

//        if (userUpdateRequestDto.profileImage() != null) {
//            BinaryContent binaryContent;
//            try {
//                binaryContent = new BinaryContent(
//                    userUpdateRequestDto.profileImage().getOriginalFilename(),
//                    userUpdateRequestDto.profileImage().getContentType(),
//                    userUpdateRequestDto.profileImage().getBytes());
//                binaryContentRepository.binaryAdd(binaryContent);
//            } catch (IOException e) {
//                throw new UncheckedIOException(
//                    "파일을 읽는데 실패했습니다: " + userUpdateRequestDto.profileImage().getOriginalFilename(),
//                    e);
//            }
//            if (Objects.nonNull(user.getProfileId())) {
//                binaryContentRepository.delete(user.getProfileId());
//            }
//            user.updateProfile(binaryContent.getId());
//        }

        user.update(userUpdateRequestDto.newUsername(), userUpdateRequestDto.newPassword(),
            userUpdateRequestDto.newEmail());

        userRepository.update(user);

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_STATUS_MISSING_FOR_USER,
                Map.of("authorId", user.getId()
                )));
        return UserDto.from(user, userStatus);
    }

    @Override
    public void userDelete(UUID userId) {
        User user = userRepository.findByUser(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_NOT_FOUND,
                Map.of("authorId", userId)
            ));

        userStatusRepository.delete(userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_STATUS_MISSING_FOR_USER,
                Map.of("authorId", user.getId()
                ))));
        if (Objects.nonNull(user.getProfileId())) {
            binaryContentRepository.delete(user.getProfileId());
        }
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAllUser();

        return users.stream()
            .map(user -> {
                UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new DiscodeitException(
                        ExceptionType.USER_STATUS_MISSING_FOR_USER,
                        Map.of("authorId", user.getId()
                        )));
                return UserDto.from(user, userStatus);
            })
            .toList();
    }

    @Override
    public UserDto findById(UUID userId) {
        User user = userRepository.findByUser(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_NOT_FOUND,
                Map.of("authorId", userId)
            ));

        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_STATUS_MISSING_FOR_USER,
                Map.of("authorId", user.getId()
                )));

        return UserDto.from(user, userStatus);
    }
}
