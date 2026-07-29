package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }


    @Override
    public Message create(String message, UUID ChannelId, UUID userId) {
        Message messsage = new Message(message, ChannelId, userId);
        messageRepository.save(messsage);
        return messsage;
    }

    @Override
    public Message read(UUID id) {

        return check(id);
    }

    @Override
    public void update(Message t) {
        Message message1 = check(t.getId());
        message1.update(t.getMessage());
        messageRepository.save(message1);
    }

    @Override
    public void delete(UUID id) {
        check(id);
        messageRepository.deleteById(id);
    }

    @Override
    public List<Message> readAll() {
        return  messageRepository.findAll();
    }


    private Message check(UUID id) {
        Message message = messageRepository.findById(id);
        if (message == null) {
            throw new IllegalArgumentException("해당 값이 존재하지 않습니다.");
        }
        return message;
    }
}
