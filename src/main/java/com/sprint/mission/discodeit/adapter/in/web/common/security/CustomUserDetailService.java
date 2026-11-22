package com.sprint.mission.discodeit.adapter.in.web.common.security;

import com.sprint.mission.discodeit.core.application.user.port.in.UserAuthenticationUseCase;
import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserAuthenticationResult;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserAuthenticationUseCase useCase;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthenticationResult result = useCase.getUserForAuthentication(username);
        return new CustomUserDetails(result.user(), result.passwordHash());
    }
}
