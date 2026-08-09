package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public record MessageUpdateRequestDto(UUID id,
                                      String message,
                                      UUID channelId,
                                      UUID authorId,
                                      List<UUID> attachmentIds) {


    public Message toEntity(){
        return new Message(attachmentIds, message, channelId, authorId);
    }
}
