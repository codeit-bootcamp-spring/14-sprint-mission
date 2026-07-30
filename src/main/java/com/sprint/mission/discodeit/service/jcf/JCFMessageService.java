package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final JCFMessageRepository messageRepository;
    private final JCFUserRepository userRepository;
    private final JCFChannelRepository channelRepository;

    public JCFMessageService(
            JCFMessageRepository messageRepository,
            JCFUserRepository userRepository,
            JCFChannelRepository channelRepository
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public MessageResponseDto create(MessageCreateRequestDto requestDto) {
        // 존재하는 유저, 채널 아이디인지 검증
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
