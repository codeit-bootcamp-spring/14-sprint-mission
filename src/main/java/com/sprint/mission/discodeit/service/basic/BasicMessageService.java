package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.entity.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    @Override
    public Message createMessage(MessageCreationDto dto) {
        String content = dto.getContent();
        UUID userId = dto.getUserId();

        Message message = new Message(content, userId);
        UUID messageId = message.getId();

        Channel channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));

        channel.addMessage(userId, messageId);
        return messageRepository.create(message);
    }

    @Override
    public Optional<Message> getMessage(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public void updateMessage(UUID id, MessageUpdateDto dto) {
        messageRepository.update(id, dto);
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.deleteById(id);
    }
}
