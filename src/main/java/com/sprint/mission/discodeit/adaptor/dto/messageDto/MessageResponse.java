package com.sprint.mission.discodeit.adaptor.dto.messageDto;

import com.sprint.mission.discodeit.domain.Message;
import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    UUID authorId,
    UUID channelId,
    String content,
    Instant createdAt,
    Instant updatedAt
) {

  public static MessageResponse from(Message message) {
    return new MessageResponse(
        message.getId(),
        message.getAuthorId(),
        message.getChannelId(),
        message.getContent(),
        message.getCreatedAt(),
        message.getUpdatedAt()
    );
  }
}
