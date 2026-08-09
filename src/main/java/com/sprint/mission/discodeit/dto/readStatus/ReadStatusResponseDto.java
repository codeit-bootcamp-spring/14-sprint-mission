package com.sprint.mission.discodeit.dto.readStatus;

import java.util.UUID;

public record ReadStatusResponseDto(
        UUID channelId,
        UUID userId
) {

    public static ReadStatusResponseDto from(UUID channelId, UUID userId){
        return new ReadStatusResponseDto(channelId, userId);
    }
}
