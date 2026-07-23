package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.entity.dto.message.MessageUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message createMessage(MessageCreationDto dto);

    Optional<Message> getMessage(UUID id);

    List<Message> getAllMessages();

    void updateMessage(UUID id, MessageUpdateDto dto);

    void deleteMessage(UUID id);
}
