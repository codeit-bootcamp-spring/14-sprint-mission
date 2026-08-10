package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public FileMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    @Override
    public Message create(Message message) {
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message find(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(Message message) {
        messageRepository.save(message);
        return message;
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}