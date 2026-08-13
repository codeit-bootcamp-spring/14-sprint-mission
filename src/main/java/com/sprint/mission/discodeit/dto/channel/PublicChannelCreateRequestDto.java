package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PublicChannelCreateRequestDto {
    String name;
    String description;

    public static PublicChannelCreateRequestDto of(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new CustomException(ExceptionType.CHANNEL_NAME_IS_NULL);
        }
        if (description == null || description.isBlank()) {
            throw new CustomException(ExceptionType.CHANNEL_DESCRIPTION_IS_NULL);
        }

        return new PublicChannelCreateRequestDto(name, description);
    }
}
