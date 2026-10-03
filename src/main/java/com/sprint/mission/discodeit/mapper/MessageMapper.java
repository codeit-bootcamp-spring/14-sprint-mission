package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

  public MessageDto toDto(Message message, List<UUID> attachmentIds) {
    return MessageDto.toDto(message, attachmentIds);
  }
}
