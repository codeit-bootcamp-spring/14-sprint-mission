package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        Message message = new Message(content, channelId, authorId);
        Message savedMessage = messageRepository.save(message);
        log.info("메시지 생성 완료 : id={}", savedMessage.getId());

        return savedMessage;
    }

    @Override
    public Message findById(UUID id) {
        Message message = Optional.ofNullable(messageRepository.findById(id))
                        .orElseThrow(() -> new IllegalArgumentException("존재하진 않는 메시지입니다."));
        log.info("메시지 조회 : id={}", message.getId());

        return message;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = messageRepository.findAll();
        log.info("메시지 전체 조회 : count={}", messages.size());

        return messages;
    }

    @Override
    public Message update(UUID id, String content) {
        Message targetMessage = messageRepository.findById(id);

        targetMessage.updateContent(content);

        Message updatedMessage = messageRepository.save(targetMessage);
        log.info("메시지 수정 완료 : id={}", updatedMessage.getId());

        return updatedMessage;
    }

    @Override
    public void delete(UUID id) {
        Optional.ofNullable(messageRepository.findById(id))
                        .orElseThrow(() -> new IllegalArgumentException("삭제할 메시지가 없습니다."));
        messageRepository.delete(id);
        log.info("메시지 삭제 완료 : id={}", id);
    }
}
