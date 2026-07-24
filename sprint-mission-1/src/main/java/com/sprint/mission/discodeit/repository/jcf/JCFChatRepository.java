package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFChatRepository implements ChatRepository {
    protected final List<Message> messages = new ArrayList<>();

    @Override
    public Message messageAdd(Message message) {
        messages.add(message);
        return message;
    }

    @Override
    public Optional<Message> findByMessage(UUID messageID) {
        return messages.stream()
                .filter(messages -> messageID.equals(messages.getMessageId()))
                .findFirst();
    }

    @Override
    public void delete(Message message) {
        messages.remove(message);
    }

    @Override
    public List<Message> findAllMessage() {
        return new ArrayList<>(messages);
    }
}
