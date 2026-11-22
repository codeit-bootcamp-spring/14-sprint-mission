package com.sprint.mission.discodeit.application.user;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.request.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.application.binarycontent.port.out.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.application.readstatus.port.out.ReadStatusPort;
import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.application.user.service.AuthService;
import com.sprint.mission.discodeit.core.application.user.service.UserApplication;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
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
    @DisplayName("User 생성 성공 시, 생성 정보를 반환")
    void whenCreateUser_thenReturnCreatedUser() {
        CreateBinaryContentCommand profile = createProfileCommand();
        when(passwordEncoder.encode("1234")).thenReturn("encoded-password");
        when(users.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserResult created = userApplication.createAccount("test", "test@gmail.com", "1234", profile);

        // then
        assertThat(created.username()).isEqualTo("test");
        assertThat(created.email()).isEqualTo("test@gmail.com");
        assertThat(created.binaryContentResult().filename()).isEqualTo(profile.fileName());
        assertThat(created.binaryContentResult().contentType()).isEqualTo(profile.contentType());
        assertThat(created.binaryContentResult().size()).isEqualTo(profile.content().length);
        verify(users).existsByUsernameOrEmail("test", "test@gmail.com");
        verify(users).save(argThat(user -> user.getPassword().equals("encoded-password")));
    }

    @Test
    @DisplayName("이미 사용중인 username 혹은 email 중복일 때, User 생성 실패 시, ")
    void givenUsernameOrEmailAlreadyExists_whenCreateUserFailed_then() {
        when(users.existsByUsernameOrEmail("existing", "existing@gmail.com")).thenReturn(true);

        // when
        UserException thrown = catchThrowableOfType(
                UserException.class,
                () -> userApplication.createAccount("existing", "existing@gmail.com", "1234", createProfileCommand())
        );

        // then
        assertThat(thrown.getType()).isEqualTo(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT);
        verify(users).existsByUsernameOrEmail("existing", "existing@gmail.com");
        verifyNoMoreInteractions(users);
        verifyNoInteractions(binaryContentStoragePort, passwordEncoder);
    }

    @Test
    @DisplayName("User 삭제 성공")
    void whenDeleteUser_thenUserDeleted() {
        UUID id = UUID.randomUUID();
        when(users.existsById(id)).thenReturn(true);

        userApplication.deleteAccount(id);

        verify(readStatuses).deleteAllByUserId(id);
        verify(users).deleteById(id);
    }

    @Test
    @DisplayName("수정 가능한 모든 인자가 주어진 경우, User 갱신 성공 시, 갱신 정보 반환")
    void givenAllArgument_whenUpdateUser_thenReturnUpdatedUser() {
        User updating = existingUser();
        UUID id = UUID.randomUUID();
        String newUsername = "newUsername";
        String newEmail = "newEmail";
        String newPassword = "newPassword";
        CreateBinaryContentCommand newProfile = createProfileCommand();
        when(users.get(id)).thenReturn(updating);
        when(passwordEncoder.encode(newPassword)).thenReturn("encoded-password");

        // when
        UserResult updated = userApplication.patchUserInfo(id, newUsername, newEmail, newPassword, newProfile);

        // then
        assertThat(updated.username()).isEqualTo(newUsername);
        assertThat(updated.email()).isEqualTo(newEmail);
        assertThat(updated.binaryContentResult().filename()).isEqualTo(newProfile.fileName());
        assertThat(updating.getPassword()).isEqualTo("encoded-password");
        verify(users).existsByUsernameAndIdNot(newUsername, id);
        verify(users).existsByEmailAndIdNot(newEmail, id);
        verify(users).get(id);
        verify(binaryContentStoragePort).put(updated.binaryContentResult().id(), newProfile.content());
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
    @DisplayName("다른 User가 이미 사용중인 username이 주어진 경우, User 갱신 실패 시, 예외를 던지고 갱신 목적으로 User를 조회하지 않는다")
    void givenDuplicatedUsername_whenUserUpdateFailed_thenThrowExceptionAndDoNotFindUserForUpdate() {
        UUID id = UUID.randomUUID();
        String duplicatedUsername = "newUsername";
        String newEmail = "newEmail";
        String newPassword = "newPassword";
        when(users.existsByUsernameAndIdNot(duplicatedUsername, id)).thenReturn(true);

        // when
        UserException thrown = catchThrowableOfType(
                UserException.class,
                () -> userApplication.patchUserInfo(id, duplicatedUsername, newEmail, newPassword, createProfileCommand())
        );

        // then
        assertThat(thrown.getType()).isEqualTo(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT);
        verify(users).existsByUsernameAndIdNot(duplicatedUsername, id);
        verifyNoMoreInteractions(users);
        verifyNoInteractions(binaryContentStoragePort, passwordEncoder);
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

    private User existingUser() {
        return User.createUser("test", "test@gmail.com", "1234",
                BinaryContent.of("profile", "image/png", new byte[]{1}));
    }

    private CreateBinaryContentCommand createProfileCommand() {
        return CreateBinaryContentCommand.of("newProfile", "image/png", new byte[]{1});
    }
}
