package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<UserResponseDto> participants,
    Instant lastMessageAt

) {

    public static ChannelResponseDto from(Channel channel, List<UserResponseDto> participants,Instant lastMessageAt) {
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
