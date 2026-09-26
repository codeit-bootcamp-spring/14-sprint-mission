package com.sprint.mission.discodeit.user.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UUID userId,
        Instant lastAccessAt,
        boolean online
) {

    public static UserStatusResponseDto from(UUID id, Instant createdAt, Instant updatedAt, UUID userId,
                                             Instant lastAccessAt, boolean online){
        return new UserStatusResponseDto(id, createdAt, updatedAt, userId, lastAccessAt, online);
    }
}
