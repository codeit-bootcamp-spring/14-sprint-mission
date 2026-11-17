package com.sprint.mission.discodeit.readStatus.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UUID userId,
        UUID channelId,
        Instant lastReadTime
) {

    public static ReadStatusResponseDto from(UUID id,
                                             Instant createdAt,
                                             Instant updatedAt,
                                             UUID userId,
                                             UUID channelId,
                                             Instant lastReadTime){
        return new ReadStatusResponseDto( id, createdAt, updatedAt,
                userId, channelId, lastReadTime);
    }
}
