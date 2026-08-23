package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(

    UUID id,
    UUID user_id,
    UUID channel_id,
    String text,
    List<UUID> attachmentIds
) {

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
            message.getId(),
            message.getUser_id(),
            message.getChannel_id(),
            message.getText(),
            message.getAttachmentIds()

        );
    }
}
