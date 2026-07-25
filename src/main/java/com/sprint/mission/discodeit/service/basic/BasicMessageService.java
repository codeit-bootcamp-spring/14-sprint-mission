package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicMessageService(MessageRepository messageRepository,
                               UserRepository userRepository,
                               ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public MessageResponseDto create(MessageCreateRequestDto requestDto) {
        // 존재하는지 검증
        userRepository.find(requestDto.getSenderId());
        channelRepository.find(requestDto.getChannelId());

        Message newMessage = Message.from(requestDto);
        messageRepository.save(newMessage);
        return MessageResponseDto.from(newMessage);
    }

    @Override
    public MessageResponseDto read(UUID id) {
        return MessageResponseDto.from(
                messageRepository.find(id)
        );
    }

    @Override
    public List<MessageResponseDto> readAll() {
        return messageRepository.findAll().stream()
                .map(MessageResponseDto::from)
                .toList();
    }

    @Override
    public MessageResponseDto update(MessageUpdateRequestDto requestDto) {
        Message messageToUpdate = messageRepository.find(requestDto.getId());
        messageToUpdate.update(requestDto.getContent());
        messageRepository.save(messageToUpdate);
        return MessageResponseDto.from(messageToUpdate);
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}
