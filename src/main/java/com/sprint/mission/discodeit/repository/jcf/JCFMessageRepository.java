package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    final List<Message> messageList;

    public JCFMessageRepository() {
        this.messageList = new ArrayList<>();
    }

    @Override
    public void save(Message message) {
        messageList.add(message);
    }

    @Override
    public Message findById(UUID id) {
        for (Message each : messageList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<Message> findAll() {
        return messageList;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageList.stream()
                .filter(m -> m.getChannelId() != null && m.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        Message target = findById(id);
        if (target != null) {
            messageList.remove(target);
        }
    }
}
