package com.sprint.mission.discodeit.message;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binaryContent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;

    public MessageDto toDto(Message message){
        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getMessage(),
                message.getChannel().getId(),
                userMapper.toDto(message.getAuthor()),
                message.getAttachments().stream().map(binaryContentMapper::toDto).toList()
        );
    }
}
