package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.sprint.mission.discodeit.entity.Message;

public record MessageDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId,
    List<UUID> attachmentIds
) {
  public static MessageDto toDto(Message message, List<UUID> attachmentIds) {
    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        message.getAuthor().getId(),
        attachmentIds
    );
  }
}
