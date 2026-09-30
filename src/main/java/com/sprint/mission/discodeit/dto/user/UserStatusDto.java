package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.domain.user.User;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(UUID id,
                            UUID userId,
                            Instant lastActiveAt) {
    public static UserStatusDto from(User user) {
        return new UserStatusDto(
                user.getStatusId(),
                user.getId(),
                user.getLastSeenAt()
        );
    }
}
