package com.sprint.misson.discodeit.repository.jcf;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.Message;
import com.sprint.misson.discodeit.entity.User;
import com.sprint.misson.discodeit.repository.MessageRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageInbox;

    public JCFMessageRepository() {
        messageInbox = new HashMap<>();
    }

    @Override
    public Message create(String text, User user, Channel channel) {
        Message message = new Message(text, user, channel);
        messageInbox.put(message.getId(),message);
        return message;
    }

    @Override
    public List<Message> readByUser(User user) {
        List<Message> messageList = messageInbox.values().stream()
            .filter(message -> message.getAuthorId().equals(user.getId()))
            .toList();
        return messageList;
    }

    @Override
    public List<Message> readByChannel(Channel channel) {
        List<Message> messageList = messageInbox.values().stream()
            .filter(message -> message.getChannelId().equals(channel.getId()))
            .toList();
        return messageList;
    }

    @Override
    public List<Message> readAll() {
        return messageInbox.values().stream().toList();
    }

    @Override
    public void update(Message message, String text) {
        message.update(text);
    }

    @Override
    public void delete(Message message) {
        messageInbox.remove(message.getId());
    }
}