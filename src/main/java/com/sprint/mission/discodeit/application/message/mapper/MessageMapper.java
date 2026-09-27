package com.sprint.mission.discodeit.application.message.mapper;

import com.sprint.mission.discodeit.application.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.application.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.application.message.dto.MessageDto;
import com.sprint.mission.discodeit.application.user.dto.UserDto;
import com.sprint.mission.discodeit.application.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.Message;
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
