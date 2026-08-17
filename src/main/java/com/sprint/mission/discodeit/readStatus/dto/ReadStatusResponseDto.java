package com.sprint.mission.discodeit.readStatus.dto;

import java.util.UUID;

public record ReadStatusResponseDto(
        UUID channelId,
        UUID userId
) {

    public static ReadStatusResponseDto from(UUID channelId, UUID userId){
        return new ReadStatusResponseDto(channelId, userId);
    }
}
