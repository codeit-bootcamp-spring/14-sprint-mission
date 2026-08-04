package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageResponseDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    public BasicMessageService(MessageRepository messageRepository,
                               ChannelService channelService,
                               UserService userService)
    {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public MessageResponseDto createMessage(MessageCreateRequestDto requestDto) {
        if (Objects.isNull(userService.readUser(requestDto.getSenderId()))
                || Objects.isNull(channelService.readChannel(requestDto.getChannelId()))) {
            throw new RuntimeException("유효하지 않은 채널 또는 유저입니다");
        }

        ChannelResponseDto channel = channelService.readChannel(requestDto.getChannelId());
        if (channel.getMemberIds() != null && channel.getMemberIds().contains(requestDto.getSenderId())) {
            Message message = new Message(requestDto.getValues(), requestDto.getChannelId(), requestDto.getSenderId());
            messageRepository.save(message);
            return MessageResponseDto.from(message);
        }

        throw new RuntimeException("해당 채널의 멤버가 아닙니다: " + requestDto.getSenderId());
    }

    @Override
    public MessageResponseDto readMessage(UUID id) {
        Message message = messageRepository.findById(id);
        return MessageResponseDto.from(message);
    }

    @Override
    public List<MessageResponseDto> readAllMessage() {
        List<Message> messages = messageRepository.findAll();
        List<MessageResponseDto> responses = new ArrayList<>();
        for (Message message : messages) {
            responses.add(MessageResponseDto.from(message));
        }
        return responses;
    }

    @Override
    public MessageResponseDto updateMessage(UUID id, MessageUpdateRequestDto requestDto) {
        Message target = messageRepository.findById(id);
        if (target == null) {
            throw new RuntimeException("해당 메시지가 존재하지 않습니다: " + id);
        }
        target.setValues(requestDto.getValues());
        target.setUpdatedAt();
        messageRepository.save(target);
        return MessageResponseDto.from(target);
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.delete(id);
    }
}
