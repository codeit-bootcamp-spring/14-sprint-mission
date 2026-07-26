package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageRepository extends CrudRepository<Message>{
    void updateContent(UUID id, String content);

    void deleteAllByUserId(UUID userId);

    void deleteAllByChannelId(UUID channelId);
}
