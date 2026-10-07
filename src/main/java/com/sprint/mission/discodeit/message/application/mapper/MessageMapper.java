package com.sprint.mission.discodeit.message.application.mapper;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.application.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.message.application.dto.MessageDto;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.application.mapper.UserMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final UserMapper userMapper;


  public MessageDto toDto(Message message) {

    UserDto userDto = userMapper.toDto(message.getAuthor());
    List<BinaryContentDto> attachments = binaryContentMapper.toList(message.getAttachments());
    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        userDto,
        attachments
    );
  }
}
