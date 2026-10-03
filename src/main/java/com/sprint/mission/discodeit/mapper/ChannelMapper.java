package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ChannelMapper {

  public ChannelDto toDto(Channel channel, List<UUID> participantIds, Instant lastMessageAt) {
    return ChannelDto.toDto(channel, participantIds, lastMessageAt);
  }
}
