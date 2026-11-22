package com.sprint.mission.discodeit.core.application.user.service;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.request.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.application.user.port.in.UserUseCase;
import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.common.annotation.ApplicationService;
import com.sprint.mission.discodeit.core.common.log.MutationLog;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.core.application.readstatus.port.out.ReadStatusPort;
import com.sprint.mission.discodeit.core.domain.user.Role;
import com.sprint.mission.discodeit.core.domain.user.User;
import com.sprint.mission.discodeit.core.domain.user.UserException;
import com.sprint.mission.discodeit.core.domain.user.UserExceptionType;
import com.sprint.mission.discodeit.core.application.binarycontent.port.out.BinaryContentStoragePort;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@ApplicationService
@RequiredArgsConstructor
public class UserApplication implements UserUseCase {
    private final UserPort users;
    private final ReadStatusPort readStatuses;
    private final BinaryContentStoragePort binaryContentStoragePort;
    private final AuthService authService;

    private final PasswordEncoder passwordEncoder;

    @MutationLog
    @Transactional
    public UserResult createAccount(
            String username,
            String email,
            String password,
            CreateBinaryContentCommand createProfileCommand
    ) {
        if (users.existsByUsernameOrEmail(username, email)) {
            throw new UserException(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT,
                    Map.of("username", username, "email", email));
        }
        BinaryContent createdProfile = createProfile(createProfileCommand);
        String encodedPassword = passwordEncoder.encode(password);
        User createdUser = users.save(User.createUser(username, email, encodedPassword, createdProfile));
        saveCreatedProfile(createProfileCommand, createdProfile);
        boolean isOnline = authService.isOnline(createdUser);
        return UserResult.from(createdUser, isOnline);
    }

    private void saveCreatedProfile(CreateBinaryContentCommand createProfileCommand, BinaryContent createdProfile) {
        if (Objects.nonNull(createdProfile)) {
            binaryContentStoragePort.put(createdProfile.getId(), createProfileCommand.content());
        }
    }

    @Transactional
    public UserResult getUser(UUID id) {
        User foundUser = users.get(id);
        boolean isOnline = authService.isOnline(foundUser);
        return UserResult.from(foundUser, isOnline);
    }

    @Transactional
    public List<UserResult> getAllUsers() {
        List<User> found = users.findAll();
        return found.stream()
                .map(user -> {
                    boolean isOnline = authService.isOnline(user);
                    return UserResult.from(user, isOnline);
                })
                .toList();
    }

    @PreAuthorize("principal.userId == #id")
    @MutationLog
    @Transactional
    public UserResult patchUserInfo(UUID id,
                              @Nullable String username, @Nullable String email, @Nullable String password,
                              CreateBinaryContentCommand createProfileCommand) {
        if (username != null && users.existsByUsernameAndIdNot(username, id)) {
            throw new UserException(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT,
                    Map.of("username", username, "id", id));
        }
        if (email != null && users.existsByEmailAndIdNot(email, id)) {
            throw new UserException(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT,
                    Map.of("email", email, "id", id));
        }
        BinaryContent createdProfile = createProfile(createProfileCommand);
        String encodedPassword = password == null ? null : passwordEncoder.encode(password);
        User updating = users.get(id);
        User updated = updating.update(username, email, encodedPassword, createdProfile);

        saveCreatedProfile(createProfileCommand, createdProfile);
        boolean isOnline = authService.isOnline(updated);
        return UserResult.from(updated, isOnline);
    }

    @PreAuthorize("principal.userId == #id")
    @MutationLog
    @Transactional
    public void deleteAccount(UUID id) {
        if (!users.existsById(id)) {
            throw new UserException(UserExceptionType.USER_NOT_FOUND, Map.of("id", id));
        }
        readStatuses.deleteAllByUserId(id);
        users.deleteById(id);
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
    @Override
    public UserResult updateUserRole(UUID userId, Role role) {
        User found = users.get(userId);
        User updated = found.updateRole(role);
        authService.removeSession(updated);
        boolean isOnline = authService.isOnline(updated);
        return UserResult.from(updated, isOnline);
    }

    @Transactional
    @Override
    public void createBootstrapAdmin(String username, String email, String password) {
        if (users.existsByRole(Role.ADMIN)) {
            return;
        }
        if (users.existsByUsernameOrEmail(username, email)) {
            throw new UserException(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT,
                    Map.of("username", username, "email", email));
        }
        String encodedPassword = passwordEncoder.encode(password);
        User admin = User.createAdmin(username, email, encodedPassword);
        users.save(admin);
    }
}
