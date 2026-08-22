package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Message create(Message message) {
        return messageRepository.create(message);
    }

    public Message findById(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.MESSAGE_NOT_FOUND_IN_DATABASE));
    }

    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }


    public Message updateContent(UUID id, String content) {
        validateExists(id);
        return messageRepository.updateContent(id, content);
    }

    public Message deleteById(UUID id) {
        validateExists(id);
        return messageRepository.deleteById(id);
    }

    public void deleteAllByUserId(UUID userId) {
        messageRepository.deleteAllByUserId(userId);
    }

    public void deleteAllByChannelId(UUID channelId) {
        messageRepository.deleteAllByChannelId(channelId);
    }

    public void validateExists(UUID id) {
        if (!messageRepository.existsById(id)) {
            throw new CustomException(ExceptionType.MESSAGE_NOT_FOUND_IN_DATABASE);
        }
    }
}
