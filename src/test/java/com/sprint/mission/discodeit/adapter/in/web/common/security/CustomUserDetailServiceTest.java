package com.sprint.mission.discodeit.adapter.in.web.common.security;

import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.application.user.port.out.UserSessionPort;
import com.sprint.mission.discodeit.core.application.user.service.UserAuthenticationApplication;
import com.sprint.mission.discodeit.core.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomUserDetailServiceTest {
    @Test
    void authenticatesUsingTheStoredPasswordHash() {
        var passwordEncoder = new BCryptPasswordEncoder();
        String username = "test-user";
        String password = "test-password";
        User user = User.createUser(username, "test@example.com", passwordEncoder.encode(password), null);
        UserPort users = mock(UserPort.class);
        when(users.get(username)).thenReturn(user);

        var useCase = new UserAuthenticationApplication(users, mock(UserSessionPort.class));
        var provider = new DaoAuthenticationProvider(new CustomUserDetailService(useCase));
        provider.setPasswordEncoder(passwordEncoder);

        var authentication = provider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, password));

        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getPrincipal()).isInstanceOfSatisfying(CustomUserDetails.class,
                principal -> assertThat(principal.getUserId()).isEqualTo(user.getId()));
        assertThatThrownBy(() -> provider.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(username, "wrong-password")))
                .isInstanceOf(BadCredentialsException.class);
    }
}
