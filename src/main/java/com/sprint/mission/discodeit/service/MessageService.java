package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message create(UUID senderId, UUID channelId, String content);
    Optional<Message> read(UUID id);
    List<Message> readAll();
    void update(UUID id, String content);
    void delete(UUID id);
}
