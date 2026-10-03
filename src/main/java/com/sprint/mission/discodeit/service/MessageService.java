package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Message create(Message message) {
        return messageRepository.save(message);
    }

    public Message findById(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.MESSAGE_NOT_FOUND_IN_DATABASE));
    }

    public Slice<Message> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable) {
        return messageRepository.findAllByChannelId(
                channelId,
                cursor,
                pageable
        );
    }

    public Message updateContent(UUID id, String content) {
        return findById(id).updateContent(content);
    }

    public Message deleteById(UUID id) {
        Message deleting = findById(id);
        messageRepository.delete(deleting);
        return deleting;
    }

    public void deleteAllByChannelId(UUID channelId) {
        messageRepository.deleteAllByChannel_Id(channelId);
    }

    public Optional<Instant> findLastMessageAtByChannelId(UUID channelId) {
        return messageRepository.findLatestMessageByChannelId(channelId);
    }
}
