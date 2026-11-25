package com.sprint.mission.discodeit.web.controller.dto.res;

import com.sprint.mission.discodeit.domain.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDTO(
    UUID id,
    UUID authorId,
    UUID channelId,
    String content,
    List<UUID> attachmentIds,
    Instant createdAt,
    Instant updatedAt
) {
    public static MessageResponseDTO from(Message message){
        return new MessageResponseDTO(
            message.getId(),
            message.getUserId(),
            message.getChannelId(),
            message.getContent(),
            message.getImageList(),
            message.getCreatedAt(),
            message.getUpdatedAt()
        );
    }

    public static List<MessageResponseDTO> fromList(List<Message> messageList){
        return messageList.stream()
            .map(MessageResponseDTO::from)
            .toList();
    }
}
