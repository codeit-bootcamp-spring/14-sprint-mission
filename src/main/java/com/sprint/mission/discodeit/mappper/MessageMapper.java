package com.sprint.mission.discodeit.mappper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    UUID author = Optional.ofNullable(message.getAuthor())
        .map(User::getId)
        .orElse(null);
    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdateAt(),
        message.getContent(),
        message.getChannel().getId(),
        author,
        attachments
    );
  }
}
