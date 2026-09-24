package com.sprint.mission.discodeit.message.mapper;

import com.sprint.mission.discodeit.binaryContent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

    @Mapping(target = "content", source = "entity.message")
    @Mapping(target = "channelId", source = "entity.channel.id")
    MessageDto toDto(Message entity);

}
