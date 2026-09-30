package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository
        extends JpaRepository<Message, UUID>, MessageRepositoryCustom{
    List<Message> findAllByChannelId(UUID channelId);

    Message updateContent(UUID id, String content);

    void deleteAllByUserId(UUID userId);

    void deleteAllByChannelId(UUID channelId);
}
