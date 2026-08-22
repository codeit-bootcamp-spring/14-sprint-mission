package com.sprint.misson.discodeit.service.file;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.Message;
import com.sprint.misson.discodeit.entity.User;
import com.sprint.misson.discodeit.repository.MessageRepository;
import com.sprint.misson.discodeit.service.MessageService;
import java.util.List;

public class FileMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public FileMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(String msg, User user, Channel channel) {
        return messageRepository.create(msg, user, channel);
    }

    @Override
    public List<Message> readByUser(User user) {
        return messageRepository.readByUser(user);
    }

    @Override
    public List<Message> readByChannel(Channel channel) {
        return messageRepository.readByChannel(channel);
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.readAll();
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
