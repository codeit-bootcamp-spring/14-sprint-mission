package com.sprint.mission.discodeit.application.user.in;

import com.sprint.mission.discodeit.application.user.UserService;
import com.sprint.mission.discodeit.adapter.in.controller.multipart.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.common.log.MutationLog;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.application.readstatus.ReadStatusService;
import com.sprint.mission.discodeit.domain.user.Role;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserStatusDto;
import com.sprint.mission.discodeit.application.binarycontent.out.BinaryContentStorage;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserApplication {
    private final UserService userService;
    private final ReadStatusService readStatusService;
    private final BinaryContentStorage binaryContentStorage;

    @MutationLog
    @Transactional
    public UserDto createAccount(
            String username,
            String email,
            String password,
            CreateBinaryContentCommand createProfileCommand
    ) {
        BinaryContent createdProfile = createProfile(createProfileCommand);
        User createdUser = userService.create(username, email, password, createdProfile);
        saveCreatedProfile(createProfileCommand, createdProfile);
        return UserDto.from(createdUser);
    }

    private void saveCreatedProfile(CreateBinaryContentCommand createProfileCommand, BinaryContent createdProfile) {
        if (Objects.nonNull(createdProfile)) {
            binaryContentStorage.put(createdProfile.getId(), createProfileCommand.content());
        }
    }

    @Transactional
    public UserDto getUser(UUID id) {
        User foundUser = userService.findById(id);
        return UserDto.from(foundUser);
    }

    @Transactional
    public List<UserDto> getAllUsers() {
        List<User> users = userService.findAll();
        return UserDto.from(users);
    }

    @MutationLog
    @Transactional
    public UserDto updateUser(UUID id,
                              @Nullable String username, @Nullable String email, @Nullable String password,
                              CreateBinaryContentCommand createProfileCommand) {

        BinaryContent createdProfile = createProfile(createProfileCommand);
        User updated = userService.update(id, username, email, password, createdProfile);
        userService.flush();
        saveCreatedProfile(createProfileCommand, createdProfile);
        return UserDto.from(updated);
    }

    @MutationLog
    @Transactional
    public void deleteAccount(UUID id) {
        userService.validateExistsById(id);
        readStatusService.deleteAllByUserId(id);
        userService.deleteById(id);
    }

    @MutationLog
    @Transactional
    public UserStatusDto updateUserStatus(UUID id, Instant newLastActiveAt) {
        User updated = userService.updateNewLastActiveAt(id, newLastActiveAt);
        return UserStatusDto.from(updated);
    }


    private @Nullable BinaryContent createProfile(CreateBinaryContentCommand profileCreateCommand) {
        if (Objects.isNull(profileCreateCommand)) {
            return null;
        }

        return BinaryContent.of(
                profileCreateCommand.fileName(),
                profileCreateCommand.contentType(),
                profileCreateCommand.content()
        );
    }

    @Transactional
    public UserDto updateUserRole(UUID userId, Role role) {
        User found = userService.findById(userId);
        User updated = found.updateRole(role);
        return UserDto.from(updated);
    }
}
