package com.sprint.mission.discodeit.channel.web.dto.res;

import com.sprint.mission.discodeit.channel.domain.entity.Channel;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDTO(
    UUID id,
    String type,
    String name,
    String description,
    List<UserResponseDTO> participants,
    Instant lastMessageAt
) {
    public static ChannelResponseDTO of(
        Channel channel,
        List<UserResponseDTO> participants,
        Instant lastMessageAt
    ) {
        return new ChannelResponseDTO(
            channel.getId(),
            channel.getChannelType().name(),
            channel.getChannelName(),
            channel.getDescription(),
            participants,
            lastMessageAt
        );
    }
}
