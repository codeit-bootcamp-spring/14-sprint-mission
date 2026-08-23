package com.sprint.mission.discodeit.message.dto;

import com.sprint.mission.discodeit.message.domain.Message;

import java.util.List;
import java.util.UUID;

public record MessageUpdateRequestDto(String newMessage
                                      ) {


//    public Message toEntity(){
//        return new Message(attachmentIds, message, channelId, authorId);
//    }
}
