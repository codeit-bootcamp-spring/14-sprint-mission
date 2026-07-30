package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.entity.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    @Override
    public Message createMessage(MessageCreationDto dto) {
        String content = dto.getContent();
        UUID userId = dto.getUserId();

        Channel channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));

        if (!channel.containsUser(userId)) {
            throw new IllegalArgumentException(String.format("Channel에 소속된 User만 Message 생성 가능. Channel: %s, User: %s", channel, userId));
        }

        Message message = new Message(content, userId, channel.getId());
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
        messageRepository.updateContent(id, dto.getContent());
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.deleteById(id);
    }
}
