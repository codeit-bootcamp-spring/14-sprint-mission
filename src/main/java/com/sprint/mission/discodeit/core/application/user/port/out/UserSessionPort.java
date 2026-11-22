package com.sprint.mission.discodeit.core.application.user.port.out;

import java.util.UUID;

public interface UserSessionPort {
    boolean isOnline(UUID userId);

    void expireSessions(UUID userId);
}
