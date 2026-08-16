package com.sprint.mission.discodeit.service.JCF;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final List<Message> messages = new ArrayList<>();

    @Override
    public Message create(Message message) {
        messages.add(message);
        return message;
    }

    @Override
    public Message find(UUID id) {
        for (Message message : messages) {
            if (message.getId().equals(id)) {
                return message;
            }
        }
        return null;
    }

    @Override
    public List<Message> findAll() {
        return messages;
    }

    @Override
    public Message update(Message message) {
        Message found = find(message.getId());

        if (found !=null){
            found.update(message.getName(), message.getCount());
        }
        return message;
    }

    @Override
    public void delete(UUID id) {

        Message found = find(id);

        if (found !=null){
            messages.remove(found);
        }
    }
}