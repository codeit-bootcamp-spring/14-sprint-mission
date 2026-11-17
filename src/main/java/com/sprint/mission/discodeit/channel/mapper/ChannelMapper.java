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
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ChannelMapper {

    @Mapping(target = "id", source = "channel.id")
    @Mapping(target = "type", source = "channel.channelType")
    @Mapping(target = "name", source = "channel.name")
    @Mapping(target = "description", source = "channel.description")
    @Mapping(target = "participants", source = "participants")
    @Mapping(target = "lastMessageAt", source = "lastMessageAt")
    ChannelDto toDto(Channel channel, List<User> participants, Instant lastMessageAt);

}
