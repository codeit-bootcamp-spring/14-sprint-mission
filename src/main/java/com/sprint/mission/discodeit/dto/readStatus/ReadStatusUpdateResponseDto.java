package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateResponseDto(
        UUID channelId,
        UUID userId,
        Instant lastReadTime
) {

    public static ReadStatusUpdateResponseDto from(UUID channelId, UUID userId, Instant lastReadTime){
        return new ReadStatusUpdateResponseDto(channelId, userId, lastReadTime);
    }
}
