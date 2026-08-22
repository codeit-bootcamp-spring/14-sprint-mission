package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageInbox;

    @Override
    public Message save(Message message) {
        messageInbox.put(message.getId(),message);
        return message;
    }

    @Override
    public Optional<Message> find(UUID id) {
        return Optional.ofNullable(messageInbox.get(id));
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        List<Message> messageList = messageInbox.values().stream()
            .filter(message -> message.getAuthorId().equals(userId))
            .toList();
        return messageList;
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        List<Message> messageList = messageInbox.values().stream()
            .filter(message -> message.getChannelId().equals(channelId))
            .toList();
        return messageList;
    }

    @Override
    public List<Message> findAll() {
        return messageInbox.values().stream().toList();
    }

    @Override
    public Message update(UUID id, String text) {
        Message message = messageInbox.get(id);
        Message updatedMessage = message.update(text);
        return messageInbox.replace(updatedMessage.getId(), updatedMessage);
    }

    @Override
    public void deleteByChannelId(UUID id) {
        List<UUID> list = messageInbox.values().stream().filter(message -> message.getChannelId().equals(id)).map(Message::getId).toList();
        for (UUID mid : list) {
            messageInbox.remove(mid);
        }
    }

    @Override
    public void delete(UUID uuid) {
        messageInbox.remove(uuid);
    }
}