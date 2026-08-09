package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDto(
        UUID userId,
        Instant lastAccessAt
) {

    public static UserStatusResponseDto from(UUID userId, Instant lastAccessAt){
        return new UserStatusResponseDto(userId, lastAccessAt);
    }
}
