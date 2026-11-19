package com.sprint.mission.discodeit.core.application.user.in;

import com.sprint.mission.discodeit.core.application.user.AuthService;
import com.sprint.mission.discodeit.core.application.user.UserService;
import com.sprint.mission.discodeit.adapter.in.controller.multipart.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.common.log.MutationLog;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.core.application.readstatus.ReadStatusService;
import com.sprint.mission.discodeit.core.domain.user.Role;
import com.sprint.mission.discodeit.core.domain.user.User;
import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.core.application.binarycontent.out.BinaryContentStorage;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserApplication {
    private final UserService userService;
    private final ReadStatusService readStatusService;
    private final BinaryContentStorage binaryContentStorage;
    private final AuthService authService;

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
        boolean isOnline = authService.isOnline(createdUser);
        return UserDto.from(createdUser, isOnline);
    }

    private void saveCreatedProfile(CreateBinaryContentCommand createProfileCommand, BinaryContent createdProfile) {
        if (Objects.nonNull(createdProfile)) {
            binaryContentStorage.put(createdProfile.getId(), createProfileCommand.content());
        }
    }

    @Transactional
    public UserDto getUser(UUID id) {
        User foundUser = userService.findById(id);
        boolean isOnline = authService.isOnline(foundUser);
        return UserDto.from(foundUser, isOnline);
    }

    @Transactional
    public List<UserDto> getAllUsers() {
        List<User> users = userService.findAll();
        return users.stream()
                .map(user -> {
                    boolean isOnline = authService.isOnline(user);
                    return UserDto.from(user, isOnline);
                })
                .toList();
    }

    @PreAuthorize("principal.userId == #id")
    @MutationLog
    @Transactional
    public UserDto updateUser(UUID id,
                              @Nullable String username, @Nullable String email, @Nullable String password,
                              CreateBinaryContentCommand createProfileCommand) {

        BinaryContent createdProfile = createProfile(createProfileCommand);
        User updated = userService.update(id, username, email, password, createdProfile);
        userService.flush();
        saveCreatedProfile(createProfileCommand, createdProfile);
        boolean isOnline = authService.isOnline(updated);
        return UserDto.from(updated, isOnline);
    }

    @PreAuthorize("principal.userId == #id")
    @MutationLog
    @Transactional
    public void deleteAccount(UUID id) {
        userService.validateExistsById(id);
        readStatusService.deleteAllByUserId(id);
        userService.deleteById(id);
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
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto updateUserRole(UUID userId, Role role) {
        User found = userService.findById(userId);
        User updated = found.updateRole(role);
        authService.removeSession(updated);
        boolean isOnline = authService.isOnline(updated);
        return UserDto.from(updated, isOnline);
    }

    @Transactional
    public void createBootstrapAdmin(String username, String email, String password) {
        userService.createBootstrapAdmin(username, email, password);
    }
}
