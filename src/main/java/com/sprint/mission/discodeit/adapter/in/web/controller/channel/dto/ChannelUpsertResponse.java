package com.sprint.mission.discodeit.adapter.in.web.controller.channel.dto;

import com.sprint.mission.discodeit.core.application.channel.port.in.dto.response.ChannelUpsertResult;
import com.sprint.mission.discodeit.core.domain.channel.ChannelType;
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
    public static ChannelUpsertResponse from(ChannelUpsertResult result) {
        return new ChannelUpsertResponse(
                result.id(),
                result.createdAt(),
                result.updatedAt(),
                result.type(),
                result.name(),
                result.description()
        );
    }
}
