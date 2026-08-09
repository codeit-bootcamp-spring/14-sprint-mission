package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.NoSuchElementException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public MessageResponseDto create(MessageCreateRequestDto request) {
        Message message = request.toEntity();
        messageRepository.save(message);
        return MessageResponseDto.from(message.getId(), message.getAttachmentIds(), message.getMessage(),
                message.getChannelId(), message.getAuthorId());
    }

    @Override
    public MessageResponseDto find(UUID id) {
        Message message = check(id);
        return MessageResponseDto.from(message.getId(), message.getAttachmentIds(), message.getMessage(),
                message.getChannelId(), message.getAuthorId());
    }

    @Override
    public void update(MessageUpdateRequestDto request) {
        Message message = check(request.id());
        message.update(request.message());
        messageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        Message message = check(id);

        List<UUID> attachmentIds = message.getAttachmentIds();

        for (UUID attachmentId : attachmentIds) {
            binaryContentRepository.deleteById(attachmentId);
        }


        messageRepository.deleteById(id);


    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .map(message -> MessageResponseDto.from(message.getId(), message.getAttachmentIds(), message.getMessage(),
                        message.getChannelId(), message.getAuthorId()))
                .toList();
    }


    private Message check(UUID id) {

        return messageRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
