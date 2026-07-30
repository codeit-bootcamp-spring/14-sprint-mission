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
public class MessageUpdateRequestDto {
    UUID id;
    String content;

    public static MessageUpdateRequestDto of(UUID id, String content) {
        if (id == null) {
            throw new CustomException(ExceptionType.MESSAGE_ID_IS_NULL);
        }

        if (content == null || content.isEmpty()) {
            throw new CustomException(ExceptionType.MESSAGE_CONTENT_IS_NULL);
        }

        return new MessageUpdateRequestDto(id, content);
    }
}
