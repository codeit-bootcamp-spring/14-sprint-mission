package com.sprint.mission.discodeit.mappper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {
  private final BinaryContentMapper binaryContentMapper;
  private final UserMapper userMapper;

  public MessageDto toDto(Message message) {
    List<BinaryContentDto> attachments = message.getAttachments().stream()
        .map(binaryContentMapper::toDto)
        .toList();
    UserDto author = Optional.ofNullable(message.getAuthor())
        .map(userMapper::toDto)
        .orElse(null);
    return new MessageDto(
        message.getId(),
        message.getCreateAt(),
        message.getUpdateAt(),
        message.getContent(),
        message.getChannel().getId(),
        author,
        attachments
    );
  }
}
