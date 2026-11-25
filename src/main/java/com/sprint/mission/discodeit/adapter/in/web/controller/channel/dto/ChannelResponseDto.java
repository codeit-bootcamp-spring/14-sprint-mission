package com.sprint.mission.discodeit.adapter.in.web.controller.channel.dto;

import com.sprint.mission.discodeit.core.application.channel.port.in.dto.response.ChannelResult;
import com.sprint.mission.discodeit.core.domain.channel.ChannelType;
import com.sprint.mission.discodeit.adapter.in.web.controller.user.dto.UserDto;
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

    public static ChannelResponseDto from(ChannelResult result) {
        List<UserDto> participants = result.participants() == null
                ? null
                : result.participants().stream().map(UserDto::from).toList();
        return new ChannelResponseDto(
                result.id(),
                result.type(),
                result.name(),
                result.description(),
                participants,
                result.lastMessageAt()
        );
    }
}
