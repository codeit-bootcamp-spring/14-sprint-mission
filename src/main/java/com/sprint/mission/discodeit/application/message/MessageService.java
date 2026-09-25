package com.sprint.mission.discodeit.application.message;

import com.sprint.mission.discodeit.adaptor.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.adaptor.dto.messageDto.MessageRequest;
import com.sprint.mission.discodeit.adaptor.dto.messageDto.MessageResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {

  MessageResponse create(MessageCreateRequest messageCreateRequest);

  List<MessageResponse> findByUser(UUID user);

  List<MessageResponse> findByChannel(UUID channelId);

  List<MessageResponse> findAll();

  MessageResponse update(UUID messageID, MessageRequest messageRequest);

  void delete(UUID uuid);
}
