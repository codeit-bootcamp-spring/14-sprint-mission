package com.sprint.mission.discodeit.core.application.user;

import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.core.config.security.CustomUserDetails;
import com.sprint.mission.discodeit.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SessionRegistry sessionRegistry;

    public void removeSession(User user) {
        CustomUserDetails principal = getPrincipal(user);
        sessionRegistry.getAllSessions(principal, false).forEach(SessionInformation::expireNow);
    }

    public boolean isOnline(User user) {
        CustomUserDetails principal = getPrincipal(user);
        return sessionsExists(principal);
    }

    private CustomUserDetails getPrincipal(User user) {
        return new CustomUserDetails(UserDto.from(user, false), user.getPassword());
    }

    private boolean sessionsExists(CustomUserDetails principal) {
        return !sessionRegistry.getAllSessions(principal, false).isEmpty();
    }
}
