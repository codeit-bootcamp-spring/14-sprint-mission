package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.domain.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponseDto(
        UUID id,
        ChannelType type,
        String name,
        String description,
        List<UUID> participantIds,
        Instant lastMessageAt
) {

    public static ChannelFindResponseDto from(
            UUID id,
            ChannelType type,
            String name,
            String description,
            List<UUID> participantIds,
            Instant lastMessageAt
    ){
        return new ChannelFindResponseDto(id, type, name, description, participantIds, lastMessageAt);
    }
}
