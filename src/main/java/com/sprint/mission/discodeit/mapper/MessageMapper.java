package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.message.domain.entity.Message;
import com.sprint.mission.discodeit.message.web.dto.res.MessageResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        BinaryContentMapper.class,
        UserMapper.class
    }
)
public interface MessageMapper {
    @Mapping(target = "channelId", source = "message.channel.id")
    @Mapping(target = "author", source = "message.user")                          // UserResponseDTO - 내부 online 조립
    @Mapping(target = "attachments", source = "message.imageList")          // BinaryRes - BM
    MessageResponseDTO toResponse(Message message);
}
// 매퍼가 더 보기힘든거같은데