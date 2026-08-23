package com.sprint.mission.discodeit.message.dto;

import com.sprint.mission.discodeit.message.domain.Message;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequestDto(
        List<UUID> attachmentIds,
        String content,
        UUID channelId,
        UUID authorId
) {

    public Message toEntity(){
        return new Message(attachmentIds, content,channelId, authorId );
    }
}
