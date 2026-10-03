package com.sprint.mission.discodeit.mappper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMApper;

  public ChannelDto toDto(Channel channel) {
    Instant lastMessage = messageRepository.findLastMessageAtByChannelId(channel.getId())
        .orElse(null);
    List<UUID> participantIds = new ArrayList<>();

    if (channel.getType().equals(ChannelType.PRIVATE))
      participantIds = readStatusRepository
          .findAllByChannelId(channel.getId())
          .stream()
          .map(ReadStatus::getUser)
          .map(User::getId)
          .toList();

    return new ChannelDto(
        channel.getId(),
        Instant.now(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        lastMessage
    );
  }

}
