package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserApplication {
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;

    public UserDto createAccount(String name,
                                 String email,
                                 String password,
                                 @Nullable MultipartFile profile) {
        UUID profileId = binaryContentRepository.create(new BinaryContent(profile)).getId();
        User created = userService.create(name, email, password, profileId);
        UserStatus userStatus = userStatusRepository.create(new UserStatus(created.getId()));
        return UserDto.of(created, userStatus);
    }

    public UserDto getUser(UUID id) {
        // 1. 사용자 온라인 정보를 포함시켜야 한다.
        // 2. 패스워드 정보는 제외해야 한다.
        User foundUser = userService.findById(id);
        UserStatus foundUserStatus = userStatusRepository.findByUserId(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USERSTATUS_NOT_FOUND_IN_DATABASE));

        return UserDto.of(foundUser, foundUserStatus);
    }

    public List<UserDto> getAllUsers() {
        // 1. 사용자 온라인 정보를 포함시켜야 한다.
        // 2. 패스워드 정보는 제외해야 한다.
        return userService.findAll().stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                            .orElseThrow(() -> new CustomException(ExceptionType.USERSTATUS_NOT_FOUND_IN_DATABASE));
                    return UserDto.of(user, userStatus);
                })
                .toList();
    }

    public UserDto updateUser(UUID id,
                              @Nullable String name, @Nullable String email, @Nullable String password,
                              @Nullable MultipartFile profile) {
        // 1. 선택적으로 프로필 이미지를 대체할 수 있어야 한다.
        // 2. DTO를 활용해 파라미터를 그룹화한다.

        UUID profileId = (Objects.nonNull(profile)) ?
                binaryContentRepository.create(new BinaryContent(profile)).getId() : null;

        User updated = userService.update(id, name, email, password, profileId);
        UserStatus userStatus = userStatusRepository.findByUserId(id).orElseThrow();
        return UserDto.of(updated, userStatus);
    }

    public UserDto deleteAccount(UUID id) {
        User toBeDeleted = userService.findById(id);
        UserStatus userStatus = userStatusRepository.findByUserId(id).orElseThrow();
        UUID profileId = toBeDeleted.getProfileId();

        // binary content가 Null일 수도 있어서 여기서 검사했는데 마음에 안듦
        // 이게 최선..?
        if (Objects.nonNull(profileId)) {
            binaryContentRepository.deleteById(toBeDeleted.getProfileId());
        }

        readStatusRepository.deleteByUserId(id);
        messageRepository.deleteAllByUserId(id);
        UserStatus deletedUserStatus = userStatusRepository.deleteByUserId(id);
        User deletedUser = userService.deleteById(id);
        return UserDto.of(deletedUser, deletedUserStatus);
    }
}
