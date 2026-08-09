package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelCreateRequestDto(
        ChannelType channelType,
        String title,
        String memo,
        List<UUID> userIds
) {
    public Channel toEntity() {

        return new Channel(channelType, title, memo);
    }
}
