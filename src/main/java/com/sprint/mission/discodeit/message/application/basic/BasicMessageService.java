package com.sprint.mission.discodeit.message.application.basic;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.message.application.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public MessageResponseDto create(MessageCreateRequestDto request, List<MultipartFile> attachments) {
        List<UUID> attachmentIds = new ArrayList<>();

        if (attachments != null) {
            for (MultipartFile file : attachments) {
                try {
                    BinaryContent binaryContent = new BinaryContent(file.getOriginalFilename(), file.getSize(),
                            file.getContentType(), file.getBytes());
                    binaryContentRepository.save(binaryContent);
                    attachmentIds.add(binaryContent.getId());
                } catch (IOException e) {
                    throw new NoSuchElementException();
                }
            }
        }

        Message message = new Message(attachmentIds, request.content(), request.channelId(), request.authorId());
        messageRepository.save(message);
        return MessageResponseDto.from(message.getId(), message.getCreatedAt(), message.getUpdatedAt(),
                message.getMessage(), message.getChannelId(),
                message.getAuthorId(), message.getAttachmentIds());
    }

    @Override
    public MessageResponseDto find(UUID id) {
        Message message = check(id);
        return MessageResponseDto.from(message.getId(), message.getCreatedAt(), message.getUpdatedAt(),
                message.getMessage(), message.getChannelId(),
                message.getAuthorId(), message.getAttachmentIds());
    }

    @Override
    public MessageResponseDto update(UUID id, MessageUpdateRequestDto request) {
        Message message = check(id);
        message.update(request.newMessage());
        messageRepository.save(message);

        return MessageResponseDto.from(message.getId(), message.getCreatedAt(), message.getUpdatedAt(),
                message.getMessage(), message.getChannelId(),
                message.getAuthorId(), message.getAttachmentIds());
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
                .map(message -> MessageResponseDto.from(message.getId(), message.getCreatedAt(), message.getUpdatedAt(),
                        message.getMessage(), message.getChannelId(),
                        message.getAuthorId(), message.getAttachmentIds()))
                .toList();
    }


    private Message check(UUID id) {

        return messageRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
