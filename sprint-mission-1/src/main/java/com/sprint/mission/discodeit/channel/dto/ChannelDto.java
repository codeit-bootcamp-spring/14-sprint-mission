package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(UUID id, ChannelType type, String name, String description,
                         List<UUID> participantIds, Instant lastMessageAt) {

    public static ChannelDto from(Channel channel) {
        return new ChannelDto(
            channel.getId(),
            channel.getType(),
            channel.getName(),
            channel.getDescription(),
            null,
            null
        );
    }

    // Public
    public static ChannelDto from(Channel channel, Instant lastMessageAt) {
        return new ChannelDto(
            channel.getId(),
            channel.getType(),
            channel.getName(),
            channel.getDescription(),
            null,
            lastMessageAt
        );
    }

    // Private
    public static ChannelDto from(Channel channel, Instant lastMessageAt,
        List<UUID> participantIds) {
        return new ChannelDto(
            channel.getId(),
            channel.getType(),
            channel.getName(),
            channel.getDescription(),
            participantIds,
            lastMessageAt
        );
    }
}
