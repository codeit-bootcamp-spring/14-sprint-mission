package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ChannelMapper {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ReadStatusRepository readStatusRepository;
    @Autowired
    private UserMapper userMapper;

    @Mapping(target = "participants", expression = "java(findParticipants(channel))")
    @Mapping(target = "lastMessageAt", expression = "java(findLastMessageAt(channel))")
    public abstract ChannelDto toDto(Channel channel);

    protected List<UserDto> findParticipants(Channel channel) {
        if (channel.getType() != ChannelType.PRIVATE) {
            return List.of();
        }
        return readStatusRepository.findAllByChannelId(channel.getId()).stream()
            .map(readStatus -> userMapper.toDto(readStatus.getUser()))
            .toList();
    }

    protected Instant findLastMessageAt(Channel channel) {
        return messageRepository.findLastMessageAtByChannelId(channel.getId())
            .orElse(null);
    }
}
