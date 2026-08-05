package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
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
        Channel channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));

        UUID userId = dto.getUserId();
        if (!channel.containsUser(dto.getUserId())) {
            throw new IllegalArgumentException(String.format("Channel에 소속된 User만 Message 생성 가능. Channel: %s, User: %s", channel, userId));
        }

        Message message = dto.toMessage();
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
    public List<Message> getAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    // TODO 구현은 나중에, 앤티티 수정해야 해서 너무 오래 걸릴 듯,,
    @Override
    public void updateMessage(UUID id, MessageUpdateDto dto) {
        messageRepository.updateContent(id, dto.getContent());
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.deleteById(id);
    }
}
