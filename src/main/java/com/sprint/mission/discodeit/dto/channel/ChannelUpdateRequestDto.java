package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelUpdateRequestDto {
    private final UUID id;
    private final String name;
    private final ChannelType channelType;

    public static ChannelUpdateRequestDto of(UUID id, String name, ChannelType channelType) {
        if (id == null) {
            throw new CustomException(ExceptionType.CHANNEL_ID_IS_NULL);
        }
        if (name == null || name.isEmpty()) {
            throw new CustomException(ExceptionType.CHANNEL_NAME_IS_NULL);
        }
        if (channelType == null) {
            throw new CustomException(ExceptionType.CHANNEL_TYPE_IS_NULL);
        }

        return new ChannelUpdateRequestDto(id, name, channelType);
    }
}
