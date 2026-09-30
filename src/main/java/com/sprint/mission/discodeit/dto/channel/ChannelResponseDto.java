package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.ChannelType;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.dto.user.UserDto;
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

    public static ChannelResponseDto of(Channel channel, List<User> participantIds, Instant lastMessageAt) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                channel.isPrivate() ? UserDto.from(participantIds) : null,
                lastMessageAt
        );
    }
}
