package com.sprint.mission.discodeit.dto.channelDto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.UUID;


public record ChannelResponse(
    UUID id,
    ChannelType type,
    String channelName,
    String description,
    Long createdAt,
    Long updatedAt,
    Long lastMessageAt,
    List<UUID> userIds
) {

    public static ChannelResponse from(
        Channel channel,
        Long recentMessageAt,
        List<UUID> userIds
    ) {
        return new ChannelResponse(
            channel.getId(),
            channel.getType(),
            channel.getChannelName(),
            channel.getDescription(),
            channel.getCreatedAt(),
            channel.getUpdatedAt(),
            recentMessageAt,
            userIds
        );
    }
}
