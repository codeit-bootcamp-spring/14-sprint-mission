package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageResponseDto(
        UUID id,
        List<UUID> attachmentIds,
        String message,
        UUID channelId,
        UUID userId) {

    public static MessageResponseDto from(UUID id,
                                          List<UUID> attachmentIds,
                                          String message,
                                          UUID channelId,
                                          UUID userId){
        return new MessageResponseDto(id, attachmentIds, message, channelId, userId);

    }
}
