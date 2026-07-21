package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.dto.MessageDto;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private static final Map<UUID, Message> data = new HashMap<>();

    @Override
    public Message create(Message message) {
        UUID id = message.getId();

        return findById(id).orElseGet(() -> {
            data.put(id, message);
            return message;
        });
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, MessageDto dto) {
        findById(id).ifPresent(retrieved -> retrieved.update(dto));
    }

    @Override
    public void deleteById(UUID id) {
        findById(id).ifPresent(retrieved -> data.remove(id));
    }
}
