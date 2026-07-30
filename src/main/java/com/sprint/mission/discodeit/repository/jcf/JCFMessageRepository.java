package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageMap = new HashMap<>();

    private JCFMessageRepository() {}

    private static class LazyHolder {
        private static final JCFMessageRepository INSTANCE = new JCFMessageRepository();
    }

    public static JCFMessageRepository getInstance() {
        return LazyHolder.INSTANCE;
    }


    @Override
    public Message save(Message message) {
        messageMap.put(message.getId(), message);
        return message;
    }

    @Override
    public Message find(UUID id) {
        return Optional.ofNullable(messageMap.get(id))
                .orElseThrow(() -> new CustomException(ExceptionType.MESSAGE_NOT_FOUND));
    }

    @Override
    public List<Message> findAll() {
        return messageMap.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        if (messageMap.remove(id) == null) {
            throw new CustomException(ExceptionType.MESSAGE_NOT_FOUND);
        }
    }
}
