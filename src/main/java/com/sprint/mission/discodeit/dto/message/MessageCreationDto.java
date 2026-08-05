package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class MessageCreationDto {
    String content;
    UUID userId;
    UUID channelId;
    List<UUID> attachmentIds;

    public Message toMessage() {
        return new Message(content, userId, channelId, attachmentIds);
    }
}
