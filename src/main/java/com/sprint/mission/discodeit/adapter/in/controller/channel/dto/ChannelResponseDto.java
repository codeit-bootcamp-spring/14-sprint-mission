package com.sprint.mission.discodeit.adapter.in.controller.channel.dto;

import com.sprint.mission.discodeit.core.domain.channel.Channel;
import com.sprint.mission.discodeit.core.domain.channel.ChannelType;
import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(@NotNull UUID id,
                                 @NotNull ChannelType type,
                                 @NotNull String name,
                                 @NotNull String description,
                                 @Nullable List<UserDto> participants,
                                 @NotNull Instant lastMessageAt) {

    public static ChannelResponseDto of(Channel channel, List<UserDto> participants, Instant lastMessageAt) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                participants,
                lastMessageAt
        );
    }
}
