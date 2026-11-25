package com.sprint.mission.discodeit.domain.service.message;

import com.sprint.mission.discodeit.domain.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message createMessage(Message message, List<UUID> imageList);
    void deleteMessage(UUID messageId);
    Message findMessageById(UUID messageId);
    List<Message> findAllMessageByChannelId(UUID channelId);
    Optional<Message> findLastMessageByChannelId(UUID channelId);
    void deleteMessageByChannelId(UUID channelId);
    Message updateMessageContent(UUID messageId, String content);
}
