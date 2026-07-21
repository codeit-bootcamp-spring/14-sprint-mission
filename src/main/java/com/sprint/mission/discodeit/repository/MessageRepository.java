package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.dto.MessageDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message create(Message message);

    Optional<Message> findById(UUID id);

    List<Message> findAll();

    void update(UUID id, MessageDto dto);

    void deleteById(UUID id);
}
