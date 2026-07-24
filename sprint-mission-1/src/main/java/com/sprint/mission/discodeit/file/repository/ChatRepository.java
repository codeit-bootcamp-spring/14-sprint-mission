package com.sprint.mission.discodeit.file.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository{
    void chatLoad();
    void chatFlush();
    void messageAdd(Message  message);
    Optional<Message> findByMessage(UUID messageID);
    void delete(Message message);
    List<Message> findAllMessage();
}
