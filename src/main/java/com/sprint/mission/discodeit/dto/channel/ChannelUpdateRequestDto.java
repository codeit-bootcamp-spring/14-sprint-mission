package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.ChannelType;
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
        return new ChannelUpdateRequestDto(id, name, channelType);
    }
}
