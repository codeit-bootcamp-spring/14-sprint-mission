package com.sprint.mission.discodeit.core.application.channel.port.in.dto.response;

import com.sprint.mission.discodeit.core.domain.channel.Channel;
import com.sprint.mission.discodeit.core.domain.channel.ChannelType;

import java.time.Instant;
import java.util.UUID;

public record ChannelUpsertResult(UUID id,
                                  Instant createdAt,
                                  Instant updatedAt,
                                  ChannelType type,
                                  String name,
                                  String description) {
    public static ChannelUpsertResult from(Channel channel) {
        return new ChannelUpsertResult(
                channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getType(),
                channel.getName(),
                channel.getDescription()
        );
    }
}
