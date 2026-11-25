package com.sprint.mission.discodeit.web.controller.dto.res;

import com.sprint.mission.discodeit.domain.entity.Channel;
import com.sprint.mission.discodeit.domain.entity.ChannelType;
import java.time.Instant;
import java.util.UUID;

public record ChannelResponseDTO(
    UUID id,
    String type,
    String name,
    String description,
    Instant createdAt,
    Instant updatedAt
) {
    public static ChannelResponseDTO from(Channel channel){
        String channelType = channel.getChannelType().equals(ChannelType.PUBLIC_CHANNEL) ? "PUBLIC" : "PRIVATE";

        return new ChannelResponseDTO(
            channel.getId(),
            channelType,
            channel.getChannelName(),
            channel.getDescription(),
            channel.getCreatedAt(),
            channel.getUpdatedAt()
        );
    }
}
