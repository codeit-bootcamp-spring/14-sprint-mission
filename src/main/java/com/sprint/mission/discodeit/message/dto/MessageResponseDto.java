package com.sprint.mission.discodeit.message.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds) {

    public static MessageResponseDto from(UUID id, Instant createdAt, Instant updatedAt, String content,
                                          UUID channelId, UUID authorId, List<UUID> attachmentIds){
        return new MessageResponseDto(id, createdAt, updatedAt, content, channelId, authorId, attachmentIds);

    }
}
