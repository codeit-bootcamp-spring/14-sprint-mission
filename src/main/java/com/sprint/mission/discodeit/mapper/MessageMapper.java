package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

  public MessageDto toDto(Message message, List<BinaryContent> attachments) {
    return MessageDto.toDto(message, attachments);
  }
}
