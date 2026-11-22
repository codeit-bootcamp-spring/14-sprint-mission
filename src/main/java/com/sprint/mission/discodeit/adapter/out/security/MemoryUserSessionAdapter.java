package com.sprint.mission.discodeit.adapter.out.security;

import com.sprint.mission.discodeit.core.application.user.port.out.UserSessionPort;
import com.sprint.mission.discodeit.core.config.security.UserSessionKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemoryUserSessionAdapter implements UserSessionPort {

    private final SessionRegistry sessionRegistry;

    @Override
    public boolean isOnline(UUID userId) {
        return !activeSessions(userId).isEmpty();
    }

    @Override
    public void expireSessions(UUID userId) {
        activeSessions(userId).forEach(SessionInformation::expireNow);
    }

    private List<SessionInformation> activeSessions(UUID userId) {
        return sessionRegistry.getAllSessions(UserSessionKey.by(userId) , false);
    }
}
