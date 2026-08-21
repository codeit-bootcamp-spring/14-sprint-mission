package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    protected Map<UUID, Message> data;

    public JCFMessageRepository(){
        data = new HashMap<>();
    }

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.values().stream()
            .filter(message -> message.getChannel_id().equals(channelId))
            .toList();
    }
}

