package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.ChannelType;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ChannelResponseDto(@NotNull UUID id,
                                 @NotNull Instant createdAt,
                                 @NotNull Instant updatedAt,
                                 @NotNull ChannelType type,
                                 @NotNull String name,
                                 @NotNull String description) {


    public static ChannelResponseDto of(Channel channel) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getChannelType(),
                channel.getName(),
                channel.getDescription()
        );
    }
}
