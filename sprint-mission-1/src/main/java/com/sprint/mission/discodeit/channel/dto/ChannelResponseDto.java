package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(UUID id, String channelName, ChannelType channelType,
                                 String description, Instant lastMessageAt,
                                 List<UUID> userIds) {

    public static ChannelResponseDto from(Channel channel) {
        return new ChannelResponseDto(
            channel.getId(),
            channel.getChannelName(),
            channel.getChannelType(),
            channel.getDescription(),
            null
            , null
        );
    }

    // Public
    public static ChannelResponseDto from(Channel channel, Instant lastMessageAt) {
        return new ChannelResponseDto(
            channel.getId(),
            channel.getChannelName(),
            channel.getChannelType(),
            channel.getDescription(),
            lastMessageAt,
            null
        );
    }

    // Private
    public static ChannelResponseDto from(Channel channel, Instant lastMessageAt,
        List<UUID> userIds) {
        return new ChannelResponseDto(
            channel.getId(),
            channel.getChannelName(),
            channel.getChannelType(),
            channel.getDescription(),
            lastMessageAt,
            userIds
        );
    }
}
