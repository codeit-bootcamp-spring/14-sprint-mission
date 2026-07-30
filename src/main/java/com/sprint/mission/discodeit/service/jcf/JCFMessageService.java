package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;
    private final Map<UUID, Message> data;

    private JCFMessageService() {
        this.data = new HashMap<>();
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
        }
        return instance;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        Message message = new Message(content, channelId, authorId);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        return data.get(messageId);
    }

    @Override
    public List<Message> findAll() {
        return data.values().stream().collect(Collectors.toList());
    }

    @Override
    public Message update(UUID messageId, String content) {
        Message message = data.get(messageId);
        if (message != null) {
            message.update(content);
        }
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        data.remove(messageId);
    }
}