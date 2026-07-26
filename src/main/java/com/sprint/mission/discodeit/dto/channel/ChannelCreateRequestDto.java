package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChannelCreateRequestDto {
    private String name;
    private ChannelType channelType;

    public static ChannelCreateRequestDto of(String name, ChannelType channelType) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Channel의 name이 비어있습니다.");
        }
        if (channelType == null) {
            throw new RuntimeException("Channel의 channelType이 비어있습니다.");
        }

        return new ChannelCreateRequestDto(name, channelType);
    }
}
