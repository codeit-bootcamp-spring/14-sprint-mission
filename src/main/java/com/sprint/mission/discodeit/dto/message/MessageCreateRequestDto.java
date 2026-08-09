package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequestDto(
        List<UUID> attachmentIds,
        String message,
        UUID channelId,
        UUID userId
) {

    public Message toEntity(){
        return new Message(attachmentIds, message,channelId, userId );
    }
}
