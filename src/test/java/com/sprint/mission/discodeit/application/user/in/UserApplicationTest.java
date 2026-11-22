package com.sprint.mission.discodeit.application.user.in;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.request.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.application.binarycontent.port.out.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.application.readstatus.port.out.ReadStatusPort;
import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.application.user.port.out.UserSessionPort;
import com.sprint.mission.discodeit.core.application.user.service.UserApplication;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.core.domain.user.Role;
import com.sprint.mission.discodeit.core.domain.user.User;
import com.sprint.mission.discodeit.core.domain.user.UserException;
import com.sprint.mission.discodeit.core.domain.user.UserExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApplicationTest {
    @Mock
    UserPort users;
    @Mock
    ReadStatusPort readStatuses;
    @Mock
    BinaryContentStoragePort binaryContentStoragePort;
    @Mock
    UserSessionPort userSessions;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserApplication userApplication;

    @Test
    @DisplayName("User 생성 성공 시, 생성 정보를 반환하고 프로필을 저장한다.")
    void whenCreateUser_thenReturnCreatedUserAndSaveProfile() {
        String username = "test";
        String email = "test@gmail.com";
        String password = "1234";
        CreateBinaryContentCommand command = createBinaryContentCommand();
        when(passwordEncoder.encode(password)).thenReturn("encoded-password");
        when(users.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserResult response = userApplication.createAccount(username, email, password, command);

        // then
        assertThat(response.username()).isEqualTo(username);
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.binaryContentResult().filename()).isEqualTo(command.fileName());
        assertThat(response.binaryContentResult().contentType()).isEqualTo(command.contentType());
        assertThat(response.binaryContentResult().size()).isEqualTo(command.content().length);
        verify(users).existsByUsernameOrEmail(username, email);
        verify(users).save(argThat(user -> user.getPassword().equals("encoded-password")));
        verify(binaryContentStoragePort).put(response.binaryContentResult().id(), command.content());
    }

    @Test
    @DisplayName("프로필 요청이 null일 때, User 생성 성공 시, 생성 정보만을 반환한다.")
    void givenNullProfile_whenCreateUser_thenOnlyReturnSavedUser() {
        String username = "test";
        String email = "test@gmail.com";
        String password = "1234";
        CreateBinaryContentCommand command = null;
        when(passwordEncoder.encode(password)).thenReturn("encoded-password");
        when(users.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserResult created = userApplication.createAccount(username, email, password, command);

        // then
        assertThat(created.username()).isEqualTo(username);
        assertThat(created.email()).isEqualTo(email);
        assertThat(created.binaryContentResult()).isNull();
        verify(users).save(any(User.class));
        verifyNoInteractions(binaryContentStoragePort);
    }

    @Test
    @DisplayName("User 생성 실패 시, 예외를 던지고 프로필을 저장하지 않는다.")
    void whenCreateFailed_thenThrowException() {
        String username = "test";
        String email = "test@gmail.com";
        String password = "1234";
        CreateBinaryContentCommand command = createBinaryContentCommand();
        when(users.existsByUsernameOrEmail(username, email)).thenReturn(true);

        // when && then
        assertThatThrownBy(() -> userApplication.createAccount(username, email, password, command))
                .isInstanceOfSatisfying(UserException.class, exception ->
                        assertThat(exception.getType()).isEqualTo(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT));
        verify(users).existsByUsernameOrEmail(username, email);
        verifyNoMoreInteractions(users);
        verifyNoInteractions(binaryContentStoragePort, passwordEncoder);
    }

    @Test
    @DisplayName("User 조회 시, 세션의 온라인 상태를 반환한다.")
    void givenOnlineUser_whenGetUser_thenReturnOnlineStatus() {
        UUID userId = UUID.randomUUID();
        User user = createUserWithId(userId, "test");
        when(users.get(userId)).thenReturn(user);
        when(userSessions.isOnline(userId)).thenReturn(true);

        // when
        UserResult response = userApplication.getUser(userId);

        // then
        assertThat(response.id()).isEqualTo(userId);
        assertThat(response.isOnline()).isTrue();
    }

    @Test
    @DisplayName("User 목록 조회 시, 사용자별 온라인 상태를 반환한다.")
    void givenUsersWithDifferentSessionStates_whenGetAllUsers_thenReturnEachOnlineStatus() {
        UUID onlineUserId = UUID.randomUUID();
        UUID offlineUserId = UUID.randomUUID();
        User onlineUser = createUserWithId(onlineUserId, "online");
        User offlineUser = createUserWithId(offlineUserId, "offline");
        when(users.findAll()).thenReturn(List.of(onlineUser, offlineUser));
        when(userSessions.isOnline(onlineUserId)).thenReturn(true);
        when(userSessions.isOnline(offlineUserId)).thenReturn(false);

        // when
        List<UserResult> responses = userApplication.getAllUsers();

        // then
        assertThat(responses).extracting(UserResult::id).containsExactly(onlineUserId, offlineUserId);
        assertThat(responses).extracting(UserResult::isOnline).containsExactly(true, false);
    }

    @Test
    @DisplayName("User 갱신 수정 성공 시, 수정 정보를 반환하고 프로필을 저장한다.")
    void whenUpdateUser_thenReturnUpdatedUser() {
        UUID userId = UUID.randomUUID();
        User existing = User.createUser("existing", "existing@gmail.com", "existing", null);
        String newUsername = "new";
        String newEmail = "new@gmail.com";
        String newPassword = "new";
        CreateBinaryContentCommand command = createBinaryContentCommand();
        when(users.get(userId)).thenReturn(existing);
        when(passwordEncoder.encode(newPassword)).thenReturn("encoded-password");

        // when
        UserResult response = userApplication.patchUserInfo(userId, newUsername, newEmail, newPassword, command);

        // then
        assertThat(response.username()).isEqualTo(newUsername);
        assertThat(response.email()).isEqualTo(newEmail);
        assertThat(response.binaryContentResult().filename()).isEqualTo(command.fileName());
        assertThat(response.binaryContentResult().contentType()).isEqualTo(command.contentType());
        assertThat(response.binaryContentResult().size()).isEqualTo(command.content().length);
        assertThat(existing.getPassword()).isEqualTo("encoded-password");
        verify(users).existsByUsernameAndIdNot(newUsername, userId);
        verify(users).existsByEmailAndIdNot(newEmail, userId);
        verify(users).get(userId);
        verify(binaryContentStoragePort).put(response.binaryContentResult().id(), command.content());
    }

    @Test
    @DisplayName("프로필 요청이 null일 때, User 수정 성공 시, 수정 정보를 반환한다.")
    void givenNullProfile_whenUpdateUser_thenReturnOnlyUpdatedUser() {
        UUID userId = UUID.randomUUID();
        User existing = User.createUser("existing", "existing@gmail.com", "existing",
                BinaryContent.of("existingProfile", "image/png", new byte[]{1}));
        String newUsername = "new";
        String newEmail = "new@gmail.com";
        String newPassword = "new";
        CreateBinaryContentCommand command = null;
        when(users.get(userId)).thenReturn(existing);
        when(passwordEncoder.encode(newPassword)).thenReturn("encoded-password");

        // when
        UserResult updated = userApplication.patchUserInfo(userId, newUsername, newEmail, newPassword, command);

        // then
        assertThat(updated.username()).isEqualTo(newUsername);
        assertThat(updated.email()).isEqualTo(newEmail);
        assertThat(updated.binaryContentResult().filename()).isEqualTo("existingProfile");
        assertThat(updated.binaryContentResult().contentType()).isEqualTo("image/png");
        assertThat(updated.binaryContentResult().size()).isEqualTo(1L);
        verify(users).get(userId);
        verifyNoInteractions(binaryContentStoragePort);
    }

    @Test
    @DisplayName("User 갱신 실패 시, 예외를 던지고 프로필을 수정하지 않는다.")
    void whenUpdateUserFailed_thenThrowExceptionAndDoNotUpdateProfile() {
        UUID userId = UUID.randomUUID();
        String newUsername = "duplicated";
        String newEmail = "duplicated@gmail.com";
        String newPassword = "new";
        CreateBinaryContentCommand command = createBinaryContentCommand();
        when(users.existsByUsernameAndIdNot(newUsername, userId)).thenReturn(true);

        // when && then
        assertThatThrownBy(() -> userApplication.patchUserInfo(userId, newUsername, newEmail, newPassword, command))
                .isInstanceOfSatisfying(UserException.class, exception ->
                        assertThat(exception.getType()).isEqualTo(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT));
        verify(users).existsByUsernameAndIdNot(newUsername, userId);
        verifyNoMoreInteractions(users);
        verifyNoInteractions(binaryContentStoragePort, passwordEncoder);
    }

    @Test
    @DisplayName("수정 가능한 일부 인자가 주어진 경우, User 갱신 성공 시, 갱신된 정보만 반환")
    void givenPartialArgument_whenUpdateUser_thenReturnPartiallyUpdatedUser() {
        User updating = existingUser();
        UUID id = UUID.randomUUID();
        String newUsername = "newUsername";
        String nullEmail = null;
        String newPassword = "newPassword";
        CreateBinaryContentCommand newProfile = createProfileCommand();
        when(users.get(id)).thenReturn(updating);
        when(passwordEncoder.encode(newPassword)).thenReturn("encoded-password");

        // when
        UserResult updated = userApplication.patchUserInfo(id, newUsername, nullEmail, newPassword, newProfile);

        // then
        assertThat(updated.username()).isEqualTo(newUsername);
        assertThat(updated.email()).isEqualTo("test@gmail.com");
        assertThat(updated.binaryContentResult().filename()).isEqualTo(newProfile.fileName());
        verify(users).existsByUsernameAndIdNot(newUsername, id);
        verify(users).get(id);
    }

    @Test
    @DisplayName("다른 User가 이미 사용중인 email 주어진 경우, User 갱신 실패 시, 예외를 던지고 갱신 목적으로 User를 조회하지 않는다")
    void givenDuplicatedEmail_whenUserUpdateFailed_thenThrowExceptionAndDoNotFindUserForUpdate() {
        UUID id = UUID.randomUUID();
        String newUsername = "newUsername";
        String duplicatedEmail = "newEmail";
        String newPassword = "newPassword";
        when(users.existsByEmailAndIdNot(duplicatedEmail, id)).thenReturn(true);

        // when
        UserException thrown = catchThrowableOfType(
                UserException.class,
                () -> userApplication.patchUserInfo(id, newUsername, duplicatedEmail, newPassword, createProfileCommand())
        );

        // then
        assertThat(thrown.getType()).isEqualTo(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT);
        verify(users).existsByUsernameAndIdNot(newUsername, id);
        verify(users).existsByEmailAndIdNot(duplicatedEmail, id);
        verifyNoMoreInteractions(users);
        verifyNoInteractions(binaryContentStoragePort, passwordEncoder);
    }

    @Test
    @DisplayName("User 삭제 성공 시, User/ReadStatus 삭제")
    void givenUserId_whenDeleteById_thenDeleteUserAndReadStatus() {
        UUID userId = UUID.randomUUID();
        when(users.existsById(userId)).thenReturn(true);

        userApplication.deleteAccount(userId);

        verify(users).existsById(userId);
        verify(readStatuses).deleteAllByUserId(userId);
        verify(users).deleteById(userId);
    }

    @Test
    @DisplayName("User 역할 변경 시, 기존 세션을 만료시키고 온라인 상태를 다시 조회한다.")
    void whenUpdateUserRole_thenExpireSessionsBeforeReadingOnlineStatus() {
        UUID userId = UUID.randomUUID();
        User user = createUserWithId(userId, "test");
        when(users.get(userId)).thenReturn(user);
        when(userSessions.isOnline(userId)).thenReturn(false);

        // when
        UserResult response = userApplication.updateUserRole(userId, Role.CHANNEL_MANAGER);

        // then
        assertThat(response.role()).isEqualTo(Role.CHANNEL_MANAGER.name());
        assertThat(response.isOnline()).isFalse();
        var sessionCalls = inOrder(userSessions);
        sessionCalls.verify(userSessions).expireSessions(userId);
        sessionCalls.verify(userSessions).isOnline(userId);
    }

    @Test
    @DisplayName("관리자가 이미 있으면 초기 관리자 생성을 건너뛴다.")
    void givenExistingAdmin_whenBootstrapping_thenSkipCreation() {
        when(users.existsByRole(Role.ADMIN)).thenReturn(true);

        userApplication.createBootstrapAdmin("admin", "admin@example.com", "password");

        verify(users).existsByRole(Role.ADMIN);
        verifyNoMoreInteractions(users);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("관리자가 없고 사용자명과 이메일을 사용할 수 있으면 초기 관리자를 생성한다.")
    void givenAvailableCredentials_whenBootstrapping_thenSaveAdmin() {
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");

        userApplication.createBootstrapAdmin("admin", "admin@example.com", "password");

        verify(users).save(argThat(user -> user.getRole() == Role.ADMIN
                && "admin".equals(user.getUsername())
                && "admin@example.com".equals(user.getEmail())
                && "encoded-password".equals(user.getPassword())));
    }

    @Test
    @DisplayName("관리자가 없고 사용자명이나 이메일이 중복이면 계정 충돌 예외를 던진다.")
    void givenConflictingCredentials_whenBootstrapping_thenThrowUserConflict() {
        when(users.existsByUsernameOrEmail("admin", "admin@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userApplication.createBootstrapAdmin("admin", "admin@example.com", "password"))
                .isInstanceOfSatisfying(UserException.class, exception ->
                        assertThat(exception.getType()).isEqualTo(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT));
        verify(users, never()).save(any(User.class));
        verifyNoInteractions(passwordEncoder);
    }

    private CreateBinaryContentCommand createBinaryContentCommand() {
        return CreateBinaryContentCommand.of("profile", "image/png", new byte[]{1});
    }

    private User existingUser() {
        return User.createUser("test", "test@gmail.com", "1234",
                BinaryContent.of("profile", "image/png", new byte[]{1}));
    }

    private CreateBinaryContentCommand createProfileCommand() {
        return CreateBinaryContentCommand.of("newProfile", "image/png", new byte[]{1});
    }

    private User createUserWithId(UUID userId, String username) {
        User user = User.createUser(username, username + "@example.com", "1234", null);
        ReflectionTestUtils.setField(user, "id", userId);
        return user;
    }
}
