package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.common.multipart.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserStatusDto;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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

    @Transactional
    public UserDto createAccount(
            String username,
            String email,
            String password,
            CreateBinaryContentCommand createProfileCommand
    ) {
        BinaryContent createdProfile = createProfile(createProfileCommand);
        User createdUser = userService.create(User.create(
                username, email, password, createdProfile
        ));
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

    @Transactional
    public UserDto updateUser(UUID id,
                              @Nullable String username, @Nullable String email, @Nullable String password,
                              CreateBinaryContentCommand createProfileCommand) {

        BinaryContent createdProfile = createProfile(createProfileCommand);
        User updating = userService.findById(id);
        User updated = userService.update(updating, username, email, password, createdProfile);
        userService.flush();
        saveCreatedProfile(createProfileCommand, createdProfile);
        return UserDto.from(updated);
    }

    @Transactional
    public void deleteAccount(UUID id) {
        readStatusService.deleteByUserId(id);
        userService.deleteById(id);
    }

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
}
