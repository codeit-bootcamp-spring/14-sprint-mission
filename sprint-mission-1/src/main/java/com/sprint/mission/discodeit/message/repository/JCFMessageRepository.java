package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.message.entity.Message;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messages = new HashMap<>();

    @Override
    public Message messageAdd(Message message) {
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findByMessage(UUID messageID) {
        return Optional.ofNullable(messages.get(messageID));
    }

    @Override
    public void delete(Message message) {
        messages.remove(message.getId());
    }

    @Override
    public void update(Message message) {
        messages.replace(message.getId(), message);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        messages.values().removeIf(message -> message.getChannelId().equals(channelId));
    }

    @Override
    public List<Message> findAllMessage(UUID channelId) {
        return messages.values().stream()
            .filter(message -> message.getChannelId().equals(channelId))
            .toList();
    }
}
