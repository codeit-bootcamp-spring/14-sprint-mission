package com.sprint.mission.discodeit.adaptor.dto.channelDto;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import java.time.Instant;
import java.util.UUID;


public record ChannelResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    ChannelType type,
    String name,
    String description
) {

  public static ChannelResponse from(
      Channel channel
  ) {
    return new ChannelResponse(
        channel.getId(),
        channel.getCreatedAt(),
        channel.getUpdatedAt(),
        channel.getType(),
        channel.getChannelName(),
        channel.getDescription()
    );
  }
}
