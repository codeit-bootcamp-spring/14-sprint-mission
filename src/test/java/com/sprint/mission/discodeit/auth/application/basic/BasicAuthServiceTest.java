package com.sprint.mission.discodeit.auth.application.basic;

import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.common.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.common.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class BasicAuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStatusRepository userStatusRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicAuthService authService;

    private User createUser(UUID id) {
        User user = User.create("kim", "kim@test.com", "password1234");
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    @Test
    @DisplayName("성공 - 비밀번호가 맞으면 마지막 접속 시간을 갱신하고 사용자 정보를 반환한다")
    void login_success() {
        // given
        UUID userId = UUID.randomUUID();
        User user = createUser(userId);
        UserStatus userStatus = UserStatus.create(user);
        Instant before = Instant.now().minusSeconds(3600);
        ReflectionTestUtils.setField(userStatus, "lastAccessAt", before);
        UserDto expected = new UserDto(userId, "kim", "kim@test.com", null, true);

        given(userRepository.findByUserName("kim")).willReturn(Optional.of(user));
        given(userStatusRepository.findByUserId(userId)).willReturn(Optional.of(userStatus));
        given(userMapper.toDto(user)).willReturn(expected);

        // when
        UserDto result = authService.login(new AuthLoginRequestDto("kim", "password1234"));

        // then
        assertThat(result).isEqualTo(expected);
        assertThat(userStatus.getLastAccessAt()).isAfter(before);
    }

    @Test
    @DisplayName("실패 - 없는 username이면 AuthenticationFailedException (401, 사용자 존재 여부를 숨긴다)")
    void login_userNotFound_fail() {
        given(userRepository.findByUserName("none")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new AuthLoginRequestDto("none", "password1234")))
                .isInstanceOf(AuthenticationFailedException.class);
        then(userMapper).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("실패 - 비밀번호가 틀리면 AuthenticationFailedException, 접속 시간을 갱신하지 않는다")
    void login_wrongPassword_fail() {
        given(userRepository.findByUserName("kim")).willReturn(Optional.of(createUser(UUID.randomUUID())));

        assertThatThrownBy(() -> authService.login(new AuthLoginRequestDto("kim", "wrong")))
                .isInstanceOf(AuthenticationFailedException.class);
        then(userStatusRepository).should(never()).findByUserId(any());
    }

    @Test
    @DisplayName("실패 - 사용자 상태 정보가 없으면 UserStatusNotFoundException")
    void login_userStatusNotFound_fail() {
        UUID userId = UUID.randomUUID();
        given(userRepository.findByUserName("kim")).willReturn(Optional.of(createUser(userId)));
        given(userStatusRepository.findByUserId(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new AuthLoginRequestDto("kim", "password1234")))
                .isInstanceOf(UserStatusNotFoundException.class);
    }
}
