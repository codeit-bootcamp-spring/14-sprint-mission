package com.sprint.mission.discodeit.dto.messageDto;

import com.sprint.mission.discodeit.entity.Message;
import java.util.UUID;

public record MessageResponse (
    UUID id,
    UUID userId,
    UUID channelId,
    String content,
    Long createdAt,
    Long updatedAt
){
    public static MessageResponse from(Message message) {
        return new MessageResponse(
            message.getId(),
            message.getAuthorId(),
            message.getChannelId(),
            message.getMsg(),
            message.getCreatedAt(),
            message.getUpdatedAt()
        );
    }
}
