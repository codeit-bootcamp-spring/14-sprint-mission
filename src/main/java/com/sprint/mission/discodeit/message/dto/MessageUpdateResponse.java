package com.sprint.mission.discodeit.message.dto;

public record MessageUpdateResponse(
        String newMessage
) {
    public static MessageUpdateResponse from(String newMessage){
        return new MessageUpdateResponse(newMessage);
    }
}
