package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.domain.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        ChannelType type,
        String name,
        String description
//        List<UUID> userIds
) {
    public static ChannelResponseDto from(UUID id,
                                          Instant createdAt,
                                          Instant updatedAt,
                                          ChannelType type,
                                          String name,
                                          String description){
        return new ChannelResponseDto(id, createdAt, updatedAt, type, name, description);
    }

}
