package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.UUID;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;

public class FileMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public FileMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(String msg, User user, UUID channel) {
        return messageRepository.save(msg, user, channel);
    }

    @Override
    public List<Message> readByUser(User user) {
        return messageRepository.findByUserId(user);
    }

    @Override
    public List<Message> readByChannel(UUID channel) {
        return messageRepository.findByChannelId(channel);
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.findAll();
    }

    @Override
    public void update(Message message, String text) {
        messageRepository.update(message, text);
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }
}
