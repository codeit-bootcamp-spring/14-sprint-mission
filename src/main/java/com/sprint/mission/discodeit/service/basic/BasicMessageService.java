package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageResponseDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomRuntimeException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    public BasicMessageService(MessageRepository messageRepository,
                               ChannelRepository channelRepository,
                               UserRepository userRepository,
                               BinaryContentRepository binaryContentRepository)
    {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public MessageResponseDto createMessage(MessageCreateRequestDto requestDto) {
        if (Objects.isNull(userRepository.findById(requestDto.getSenderId()))
                || Objects.isNull(channelRepository.findById(requestDto.getChannelId()))) {
            // throw new RuntimeException("유효하지 않은 채널 또는 유저입니다");
            throw new CustomRuntimeException(ExceptionType.DATABASE_CONNECTION_FAILED);
        }

        Channel channel = channelRepository.findById(requestDto.getChannelId());
        if (channel.getMemberIds() != null && channel.getMemberIds().contains(requestDto.getSenderId())) {
            Message message = new Message(requestDto.getValues(), requestDto.getChannelId(), requestDto.getSenderId());
            messageRepository.save(message);

            List<BinaryContentResponseDto> addedContents = new ArrayList<>();
            if (requestDto.getAttachmentDtos() != null && !requestDto.getAttachmentDtos().isEmpty()) {
                for (BinaryContentCreateRequestDto attachmentDto : requestDto.getAttachmentDtos()) {
                    BinaryContent binaryContent = attachmentDto.toEntity();
                    binaryContentRepository.save(binaryContent);
                    addedContents.add(BinaryContentResponseDto.from(binaryContent));
                }
            }

            return MessageResponseDto.from(message, addedContents);
        }

        // throw new RuntimeException("해당 채널의 멤버가 아닙니다: " + requestDto.getSenderId());
        throw new CustomRuntimeException(ExceptionType.DATABASE_CONNECTION_FAILED);
    }

    @Override
    public MessageResponseDto readMessage(UUID id) {
        Message message = messageRepository.findById(id);
        if (Objects.isNull(message)) {
            // throw new RuntimeException("해당 메시지가 존재하지 않습니다: " + id);
            throw new CustomRuntimeException(ExceptionType.NOT_FOUND);
        }
        List<BinaryContentResponseDto> addedContents = getAttachments(id);
        return MessageResponseDto.from(message, addedContents);
    }

    @Override
    public List<MessageResponseDto> readAllMessage() {
        List<Message> messages = messageRepository.findAll();
        List<MessageResponseDto> responses = new ArrayList<>();
        for (Message message : messages) {
            List<BinaryContentResponseDto> addedContents = getAttachments(message.getId());
            responses.add(MessageResponseDto.from(message, addedContents));
        }
        return responses;
    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        List<MessageResponseDto> responses = new ArrayList<>();
        for (Message message : messages) {
            List<BinaryContentResponseDto> addedContents = getAttachments(message.getId());
            responses.add(MessageResponseDto.from(message, addedContents));
        }
        return responses;
    }

    @Override
    public MessageResponseDto updateMessage(UUID id, MessageUpdateRequestDto requestDto) {
        Message target = messageRepository.findById(id);
        if (target == null) {
            // throw new RuntimeException("해당 메시지가 존재하지 않습니다: " + id);
            throw new CustomRuntimeException(ExceptionType.NOT_FOUND);
        }
        target.setValues(requestDto.getValues());
        target.setUpdatedAt();
        messageRepository.save(target);
        List<BinaryContentResponseDto> addedContents = getAttachments(id);
        return MessageResponseDto.from(target, addedContents);
    }

    @Override
    public void deleteMessage(UUID id) {
        List<BinaryContent> attachments = binaryContentRepository.findAllByMessageId(id);
        for (BinaryContent attachment : attachments) {
            binaryContentRepository.delete(attachment.getId());
        }
        messageRepository.delete(id);
    }

    private List<BinaryContentResponseDto> getAttachments(UUID messageId) {
        List<BinaryContent> contents = binaryContentRepository.findAllByMessageId(messageId);
        List<BinaryContentResponseDto> dtos = new ArrayList<>();
        for (BinaryContent content : contents) {
            dtos.add(BinaryContentResponseDto.from(content));
        }
        return dtos;
    }
}
