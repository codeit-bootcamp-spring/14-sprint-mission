package com.sprint.mission.discodeit.readStatus.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {

    public static ReadStatusUpdateResponseDto from(UUID id, Instant createdAt, Instant updatedAt,
                                                   UUID userId, UUID channelId, Instant lastReadAt){
        return new ReadStatusUpdateResponseDto(id, createdAt, updatedAt, userId, channelId, lastReadAt);

    }
}
