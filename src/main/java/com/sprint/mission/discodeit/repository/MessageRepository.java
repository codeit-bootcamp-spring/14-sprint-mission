package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);
    Optional<Message> findById(UUID id); // Optional 로 감싸기
    List<Message> findAll();
    void deleteById(UUID id);

    void deleteByChannelId(UUID ChannelId);

    List<Message> findAllByChannelId(UUID channelId);
}
