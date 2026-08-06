package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.message.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

    Message messageAdd(Message message);

    Optional<Message> findByMessage(UUID messageID);

    void delete(Message message);

    void update(Message message);

    void deleteByChannelId(UUID channelId);

    List<Message> findAllMessage(UUID channelId);
}
