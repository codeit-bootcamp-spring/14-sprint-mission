package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(

    UUID id,
    UUID authorId,
    UUID channelId,
    String content,
    List<UUID> attachmentIds,
    Instant createdAt,
    Instant updatedAt
) {

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
            message.getId(),
            message.getAuthor() != null ? message.getAuthor().getId() : null,
            message.getChannel().getId(),
            message.getContent(),
            message.getAttachments().stream()
                .map(BinaryContent::getId)
                .toList(),
            message.getCreatedAt(),
            message.getUpdatedAt()

        );
    }
}
