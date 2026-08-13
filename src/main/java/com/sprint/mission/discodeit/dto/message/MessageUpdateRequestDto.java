package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageUpdateRequestDto {
    String content;

    public static MessageUpdateRequestDto of(String content) {
        if (Objects.isNull(content) || content.isBlank()) {
            throw new CustomException(ExceptionType.MESSAGE_CONTENT_IS_NULL);
        }

        return new MessageUpdateRequestDto(content);
    }
}
