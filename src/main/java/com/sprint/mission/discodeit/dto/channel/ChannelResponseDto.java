package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.ChannelType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(@NotNull UUID id,
                                 @NotNull ChannelType type,
                                 @NotNull String name,
                                 @NotNull String description,
                                 @Nullable List<UUID> participantIds,
                                 @NotNull Instant lastMessageAt) {

    public static ChannelResponseDto of(Channel channel, List<UUID> participantIds) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getChannelType(),
                channel.getName(),
                channel.getDescription(),
                channel.isPrivate() ? participantIds : null,
                channel.getUpdatedAt()
        );
    }
}
