package com.sprint.mission.discodeit.message.dto;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequestDto(
        @NotBlank(message = "수정할 메시지 내용은 필수입니다.")
        String newContent
) {


//    public Message toEntity(){
//        return new Message(attachmentIds, message, channelId, authorId);
//    }
}
