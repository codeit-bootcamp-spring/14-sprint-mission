package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageResponse;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageResponse create(MessageCreateRequest messageCreateRequest);

  List<MessageResponse> findByUser(UUID user);

  List<MessageResponse> findByChannel(UUID channelId);

  List<MessageResponse> findAll();

  MessageResponse update(MessageRequest messageRequest);

  void delete(UUID uuid);
}
