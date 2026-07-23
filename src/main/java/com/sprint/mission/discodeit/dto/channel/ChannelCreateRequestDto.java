package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.ChannelType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChannelCreateRequestDto {
    private String name;
    private ChannelType channelType;

    public static ChannelCreateRequestDto of(String name, ChannelType channelType) {
        return new ChannelCreateRequestDto(name, channelType);
    }
}
