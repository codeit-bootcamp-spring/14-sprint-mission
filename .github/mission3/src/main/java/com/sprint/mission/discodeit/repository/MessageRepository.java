package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

    Message save(Message message);

    Optional<Message> find(UUID id);

    List<Message> findByUserId(UUID userId);

    List<Message> findByChannelId(UUID channelId);

    List<Message> findAll();

    Message update(UUID id, String text);

    void deleteByChannelId(UUID id);

    void delete(UUID uuid);
}
