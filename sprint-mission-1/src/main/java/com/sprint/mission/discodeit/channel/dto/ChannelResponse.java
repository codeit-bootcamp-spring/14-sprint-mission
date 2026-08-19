package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import java.time.Instant;
import java.util.UUID;

public record ChannelResponse(UUID id, Instant createdAt, Instant updatedAt, ChannelType type,
                              String name, String description) {

    public static ChannelResponse from(Channel channel) {
        return new ChannelResponse(
            channel.getId(),
            channel.getCreatedAt(),
            channel.getUpdatedAt(),
            channel.getType(),
            channel.getName(),
            channel.getDescription()
        );
    }
}
