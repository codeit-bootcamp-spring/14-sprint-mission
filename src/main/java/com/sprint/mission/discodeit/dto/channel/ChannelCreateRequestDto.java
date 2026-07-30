package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChannelCreateRequestDto {
    String name;
    ChannelType channelType;

    public static ChannelCreateRequestDto of(String name, ChannelType channelType) {
        if (name == null || name.isEmpty()) {
            throw new CustomException(ExceptionType.CHANNEL_NAME_IS_NULL);
        }
        if (channelType == null) {
            throw new CustomException(ExceptionType.CHANNEL_TYPE_IS_NULL);
        }

        return new ChannelCreateRequestDto(name, channelType);
    }
}
