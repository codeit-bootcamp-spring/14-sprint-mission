package com.sprint.mission.discodeit.auth.security;

import com.sprint.mission.discodeit.user.service.UserOnlineChecker;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionUserOnlineChecker implements UserOnlineChecker {

    private final SessionRegistry sessionRegistry;

    // 만료되지 않은 세션이 하나라도 있으면 접속 중으로 본다
    @Override
    public boolean isOnline(UUID userId) {
        return sessionRegistry.getAllPrincipals().stream()
            .filter(principal -> principal instanceof DiscodeitUserDetails details
                && details.getUserDto().id().equals(userId))
            .anyMatch(principal -> !sessionRegistry.getAllSessions(principal, false).isEmpty());
    }
}
