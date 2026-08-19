package com.sprint.mission.discodeit.message.dto;

import com.sprint.mission.discodeit.message.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(UUID userId, UUID channelId, UUID messageId, String message,
                                 Instant createdAt,
                                 List<UUID> binaryContentsId) {

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
            message.getAuthorId(),
            message.getChannelId(),
            message.getId(),
            message.getContent(),
            message.getCreatedAt(),
            message.getAttachmentIds()
        );
    }
}
