package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.domain.userstatus.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(UUID userId,
                            Instant lastActiveAt,
                            Boolean online) {
    public static UserStatusDto of(UserStatus userStatus) {
        return new UserStatusDto(
                userStatus.getUserId(),
                userStatus.getLastSeenAt(),
                userStatus.isOnline()
        );
    }
}
