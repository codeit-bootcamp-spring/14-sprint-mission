package com.sprint.mission.discodeit.message.mapper;

import com.sprint.mission.discodeit.binaryContent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

    @Mapping(target = "content", source = "message.content")
    @Mapping(target = "channelId", source = "message.channel.id")
    MessageDto toDto(Message message);

}
