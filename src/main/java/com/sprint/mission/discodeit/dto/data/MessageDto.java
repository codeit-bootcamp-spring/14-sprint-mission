package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

public record MessageDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId,
    List<UUID> attachmentIds,
    List<BinaryContentDto> attachments,
    UserDto author
) {
  public static MessageDto toDto(Message message, List<BinaryContent> attachments) {
    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        message.getAuthor().getId(),
        attachments.stream().map(BinaryContent::getId).toList(),
        attachments.stream().map(BinaryContentDto::toDto).toList(),
        UserDto.toDto(message.getAuthor(), null)
    );
  }
}
