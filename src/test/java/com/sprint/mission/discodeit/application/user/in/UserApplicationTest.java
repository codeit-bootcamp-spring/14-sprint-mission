package com.sprint.mission.discodeit.application.user.in;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.request.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.application.binarycontent.port.out.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.application.readstatus.port.out.ReadStatusPort;
import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.application.user.service.AuthService;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    AuthService authService;
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
        verify(users).save(any(User.class));
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
        verifyNoInteractions(binaryContentStoragePort);
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
        verifyNoInteractions(binaryContentStoragePort);
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
}
