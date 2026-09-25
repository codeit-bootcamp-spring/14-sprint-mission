package com.sprint.mission.discodeit.adaptor.dto.channelDto;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<UUID> participantIds,
    Instant lastMessageAt
) {

  public static ChannelDto from(Channel channel, Instant lastMessageAt,
      List<UUID> participantIds) {
    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getChannelName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }
}
