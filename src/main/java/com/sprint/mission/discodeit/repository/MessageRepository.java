package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.message.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends CrudRepository<Message>{
    List<Message> findAllByChannelId(UUID channelId);

    Message updateContent(UUID id, String content);

    void deleteAllByUserId(UUID userId);

    void deleteAllByChannelId(UUID channelId);

    Optional<Instant> findLatestMessageByChannelId(UUID channelId);
}
