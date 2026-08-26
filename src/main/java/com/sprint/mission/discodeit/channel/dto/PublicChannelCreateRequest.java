package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;

import java.util.List;
import java.util.UUID;

public record PublicChannelCreateRequest(
        String name,
        String description
) {
    public Channel toEntity() {

        return new Channel(ChannelType.PUBLIC, name, description);
    }
}
