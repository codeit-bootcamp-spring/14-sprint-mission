package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public JCFMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(Message message) {
        messageRepository.save(message);
        System.out.println("메시지 생성이 완료되었습니다.");

        return message;
    }

    @Override
    public Message findById(UUID id) {
        Message message = Optional.ofNullable(messageRepository.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메시지입니다."));

        return message;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID id, Message message) {
        Message updatedMessage = Optional.ofNullable(messageRepository.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("수정할 메시지가 없습니다."));

        updatedMessage.setContent(message.getContent());

        messageRepository.save(updatedMessage);
        System.out.println("메시지 정보 수정이 완료되었습니다.");

        return updatedMessage;
    }

    @Override
    public void delete(UUID id) {
        Message deletedMessage = Optional.ofNullable(messageRepository.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("삭제할 메시지가 없습니다."));

        messageRepository.delete(id);
        System.out.println("메시지 정보 삭제가 완료되었습니다.");
    }
}
