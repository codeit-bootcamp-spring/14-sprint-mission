package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(

    UUID id,
    UUID userId,
    UUID channelId,
    String text,
    List<UUID> attachmentIds
) {

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
            message.getId(),
            message.getAuthor() != null ? message.getAuthor().getId() : null,
            message.getChannel().getId(),
            message.getContent(),
            message.getAttachments().stream()
                .map(BinaryContent::getId)
                .toList()

        );
    }
}
