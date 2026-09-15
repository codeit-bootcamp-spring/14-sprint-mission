package com.sprint.mission.discodeit.channel.mapper;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final UserMapper userMapper;

    public ChannelDto toDto(Channel channel, List<User> participants, Instant lastMessageAt){

        return new ChannelDto(
                channel.getId(),
                channel.getChannelType(),
                channel.getName(),
                channel.getDescription(),
                participants.stream().map(userMapper::toDto).toList(),
                lastMessageAt
        );
    }

}
