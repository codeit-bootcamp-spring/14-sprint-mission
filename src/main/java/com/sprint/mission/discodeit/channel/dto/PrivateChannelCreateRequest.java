package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        List<UUID> participantIds
) {
    public Channel toEntity() {

        return new Channel(ChannelType.PRIVATE,null, null);
    }
}
