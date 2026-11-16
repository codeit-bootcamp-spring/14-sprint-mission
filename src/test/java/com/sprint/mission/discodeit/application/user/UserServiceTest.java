package com.sprint.mission.discodeit.application.user;

import com.sprint.mission.discodeit.application.user.out.UserRepository;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.UserException;
import com.sprint.mission.discodeit.domain.user.UserExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("User 생성 성공 시, 생성 정보를 반환")
    void whenCreateUser_thenReturnCreatedUser() {
        User creating = User.create(
                "test",
                "test@gmail.com",
                "1234",
                BinaryContent.of("profile", "png", new byte[]{1})
        );
        when(userRepository.existsByUsernameOrEmail(creating.getUsername(), creating.getEmail()))
                .thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(creating.getPassword())).thenReturn(creating.getPassword());

        // when
        User created = userService.create(creating.getUsername(), creating.getEmail(), creating.getPassword(), creating.getProfile());

        // then
        assertThat(created.getUsername()).isEqualTo(creating.getUsername());
        assertThat(created.getEmail()).isEqualTo(creating.getEmail());
        assertThat(created.getProfile().getFileName()).isEqualTo(creating.getProfile().getFileName());
        assertThat(created.getProfile().getContentType()).isEqualTo(creating.getProfile().getContentType());
        assertThat(created.getProfile().getSize()).isEqualTo(creating.getProfile().getSize());

        verify(userRepository).existsByUsernameOrEmail(creating.getUsername(), creating.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("이미 사용중인 username 혹은 email 중복일 때, User 생성 실패 시, ")
    void givenUsernameOrEmailAlreadyExists_whenCreateUserFailed_then() {
        User creating = User.create(
                "existing",
                "existing@gmail.com",
                "1234",
                BinaryContent.of("profile", "png", new byte[]{1})
        );

        when(userRepository.existsByUsernameOrEmail(creating.getUsername(), creating.getEmail()))
                .thenReturn(true);

        // when
        UserException thrown = catchThrowableOfType(
                UserException.class,
                () -> userService.create(creating.getUsername(), creating.getEmail(), creating.getPassword(), creating.getProfile())
        );

        // then
        assertThat(thrown.getType()).isEqualTo(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT);

        verify(userRepository).existsByUsernameOrEmail(creating.getUsername(), creating.getEmail());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("User 삭제 성공")
    void whenDeleteUser_thenUserDeleted() {
        UUID id = UUID.randomUUID();
        User deleting = User.create(
                "test",
                "test@gmail.com",
                "1234",
                BinaryContent.of("profile", "png", new byte[]{1})
        );

        when(userRepository.findById(id)).thenReturn(Optional.of(deleting));

        userService.deleteById(id);

        verify(userRepository).delete(deleting);
    }

    @Test
    @DisplayName("수정 가능한 모든 인자가 주어진 경우, User 갱신 성공 시, 갱신 정보 반환")
    void givenAllArgument_whenUpdateUser_thenReturnUpdatedUser() {
        User updating = User.create(
                "test",
                "test@gmail.com",
                "1234",
                BinaryContent.of("profile", "png", new byte[]{1})
        );
        UUID id = UUID.randomUUID();
        String newUsername = "newUsername";
        String newEmail = "newEmail";
        String newPassword = "newPassword";
        BinaryContent newProfile = BinaryContent.of("newProfile", "png", new byte[]{1});

        when(userRepository.existsByUsernameAndIdNot(newUsername, id))
                .thenReturn(false);
        when(userRepository.existsByEmailAndIdNot(newEmail, id))
                .thenReturn(false);
        when(userRepository.findById(id))
                .thenReturn(Optional.of(updating));

        // when
        User updated = userService.update(id, newUsername, newEmail, newPassword, newProfile);

        // then
        assertThat(updated.getUsername()).isEqualTo(newUsername);
        assertThat(updated.getEmail()).isEqualTo(newEmail);
        assertThat(updated.getProfile()).isEqualTo(newProfile);

        verify(userRepository).existsByUsernameAndIdNot(newUsername, id);
        verify(userRepository).existsByEmailAndIdNot(newEmail, id);
        verify(userRepository).findById(id);
    }

    @Test
    @DisplayName("수정 가능한 일부 인자가 주어진 경우, User 갱신 성공 시, 갱신된 정보만 반환")
    void givenPartialArgument_whenUpdateUser_thenReturnPartiallyUpdatedUser() {
        User updating = User.create(
                "test",
                "test@gmail.com",
                "1234",
                BinaryContent.of("profile", "png", new byte[]{1})
        );
        UUID id = UUID.randomUUID();
        String newUsername = "newUsername";
        String nullEmail = null;
        String newPassword = "newPassword";
        BinaryContent newProfile = BinaryContent.of("newProfile", "png", new byte[]{1});

        when(userRepository.existsByUsernameAndIdNot(newUsername, id))
                .thenReturn(false);
        when(userRepository.existsByEmailAndIdNot(nullEmail, id))
                .thenReturn(false);
        when(userRepository.findById(id))
                .thenReturn(Optional.of(updating));

        // when
        User updated = userService.update(id, newUsername, nullEmail, newPassword, newProfile);

        // then
        assertThat(updated.getUsername()).isEqualTo(newUsername);
        assertThat(updated.getEmail()).isEqualTo(updating.getEmail());
        assertThat(updated.getProfile()).isEqualTo(newProfile);

        verify(userRepository).existsByUsernameAndIdNot(newUsername, id);
        verify(userRepository).existsByEmailAndIdNot(nullEmail, id);
        verify(userRepository).findById(id);
    }

    @Test
    @DisplayName("다른 User가 이미 사용중인 username이 주어진 경우, User 갱신 실패 시, 예외를 던지고 갱신 목적으로 User를 조회하지 않는다")
    void givenDuplicatedUsername_whenUserUpdateFailed_thenThrowExceptionAndDoNotFindUserForUpdate() {
        UUID id = UUID.randomUUID();
        String duplicatedUsername = "newUsername";
        String newEmail = "newEmail";
        String newPassword = "newPassword";
        BinaryContent newProfile = BinaryContent.of("newProfile", "png", new byte[]{1});

        when(userRepository.existsByUsernameAndIdNot(duplicatedUsername, id))
                .thenReturn(true);

        // when
        UserException thrown = catchThrowableOfType(
                UserException.class,
                () -> userService.update(id, duplicatedUsername, newEmail, newPassword, newProfile)
        );

        // then
        assertThat(thrown.getType()).isEqualTo(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT);

        verify(userRepository).existsByUsernameAndIdNot(duplicatedUsername, id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("다른 User가 이미 사용중인 email 주어진 경우, User 갱신 실패 시, 예외를 던지고 갱신 목적으로 User를 조회하지 않는다")
    void givenDuplicatedEmail_whenUserUpdateFailed_thenThrowExceptionAndDoNotFindUserForUpdate() {
        UUID id = UUID.randomUUID();
        String newUsername = "newUsername";
        String duplicatedEmail = "newEmail";
        String newPassword = "newPassword";
        BinaryContent newProfile = BinaryContent.of("newProfile", "png", new byte[]{1});

        when(userRepository.existsByUsernameAndIdNot(newUsername, id))
                .thenReturn(false);
        when(userRepository.existsByEmailAndIdNot(duplicatedEmail, id))
                .thenReturn(true);

        // when
        UserException thrown = catchThrowableOfType(
                UserException.class,
                () -> userService.update(id, newUsername, duplicatedEmail, newPassword, newProfile)
        );

        // then
        assertThat(thrown.getType()).isEqualTo(UserExceptionType.USER_UNIQUE_FIELD_CONFLICT);

        verify(userRepository).existsByUsernameAndIdNot(newUsername, id);
        verify(userRepository).existsByEmailAndIdNot(duplicatedEmail, id);
        verifyNoMoreInteractions(userRepository);
    }
}
