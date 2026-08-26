package com.sprint.mission.discodeit.web.controller.dto.res;

import com.sprint.mission.discodeit.domain.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDTO(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    Instant lastActiveAt,
    boolean online
) {
    public static UserStatusResponseDTO from(UserStatus userStatus){
        return new UserStatusResponseDTO(
            userStatus.getUserId(),
            userStatus.getCreatedAt(),
            userStatus.getUpdatedAt(),
            userStatus.getUserId(),
            userStatus.getLastActiveAt(),
            userStatus.isOnline()
            );
    }
}
