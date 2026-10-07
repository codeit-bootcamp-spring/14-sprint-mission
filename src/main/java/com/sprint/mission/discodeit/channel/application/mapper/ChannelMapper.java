package com.sprint.mission.discodeit.channel.application.mapper;

import com.sprint.mission.discodeit.channel.application.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.application.mapper.UserMapper;
import com.sprint.mission.discodeit.user.domain.User;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel, List<User> participants, Instant lastMessageAt) {
    List<UserDto> userDtos = participants.stream().map(userMapper::toDto).toList();
    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        userDtos,
        lastMessageAt
    );
  }
}
