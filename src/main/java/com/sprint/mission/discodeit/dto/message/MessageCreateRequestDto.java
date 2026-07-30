package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageCreateRequestDto {
    String content;
    UUID senderId;
    UUID channelId;

    public static MessageCreateRequestDto of(String content, UUID senderId, UUID channelId) {
        if (content == null || content.isEmpty()) {
            throw new CustomException(ExceptionType.MESSAGE_CONTENT_IS_NULL);
        }
        if (senderId == null) {
            throw new CustomException(ExceptionType.MESSAGE_SENDER_ID_IS_NULL);
        }
        if (channelId == null) {
            throw new CustomException(ExceptionType.MESSAGE_CHANNEL_ID_IS_NULL);
        }


        return new MessageCreateRequestDto(content,senderId, channelId);
    }
}
