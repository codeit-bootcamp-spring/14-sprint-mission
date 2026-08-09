package com.sprint.mission.discodeit.service.domain.message;

import com.sprint.mission.discodeit.domain.Message;

import java.util.List;
import java.util.UUID;

public interface MessageDomainService {
    Message create(Message message);

    Message findById(UUID messageId);

    List<Message> findAll();

    List<Message> findAllByChannelId(UUID channelId);

    Message findMostRecentByChannelId(UUID channelId);

    Message update(
            UUID messageId,
            String updatedContent
    );

    void delete(UUID messageId);

    void deleteAllByChannelId(UUID channelId);
}
