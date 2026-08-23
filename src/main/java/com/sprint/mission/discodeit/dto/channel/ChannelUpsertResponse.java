package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.ChannelType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ChannelUpsertResponse(@NotNull UUID id,
                                    @NotNull Instant createdAt,
                                    @NotNull Instant updatedAt,
                                    @NotNull ChannelType type,
                                    @NotNull String name,
                                    @Nullable String description) {
    public static ChannelUpsertResponse of(Channel created) {
        return new ChannelUpsertResponse(
                created.getId(),
                created.getCreatedAt(),
                created.getUpdatedAt(),
                created.getChannelType(),
                created.getName(),
                created.getDescription()
        );
    }
}
