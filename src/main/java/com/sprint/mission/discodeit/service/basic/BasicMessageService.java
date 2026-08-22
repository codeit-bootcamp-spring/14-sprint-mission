package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    public BasicMessageService(
            MessageRepository messageRepository,
            ChannelRepository channelRepository,
            UserRepository userRepository,
            BinaryContentRepository binaryContentRepository
    ) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public MessageDto create(MessageCreateRequest request, List<BinaryContentCreateRequest> attachmentRequests) {
        // 다른 서비스가 아니라 저장소를 직접 보게끔..
        // 서비스끼리 물고 물리면 어느 쪽을 먼저 만들어야 하는지가 생김.
        channelRepository.findById(request.channelId())
                .orElseThrow(() -> new DiscodeitException(ExceptionType.CHANNEL_NOT_FOUND,
                        "채널을 찾을 수 없습니다! id: " + request.channelId()));
        userRepository.findById(request.authorId())
                .orElseThrow(() -> new DiscodeitException(ExceptionType.USER_NOT_FOUND,
                        "유저를 찾을 수 없습니다! id: " + request.authorId()));

        List<UUID> attachmentIds = new ArrayList<>();
        if (attachmentRequests != null) {
            for (BinaryContentCreateRequest attachmentRequest : attachmentRequests) {
                BinaryContent attachment = new BinaryContent(
                        attachmentRequest.fileName(), attachmentRequest.contentType(), attachmentRequest.bytes());
                attachmentIds.add(binaryContentRepository.save(attachment).getId());
            }
        }

        Message message = new Message(request.content(), request.channelId(), request.authorId(), attachmentIds);
        return toDto(messageRepository.save(message));
    }

    @Override
    public MessageDto find(UUID messageId) {
        return toDto(findEntity(messageId));
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest request) {
        Message message = findEntity(messageId);
        message.update(request.newContent());
        return toDto(messageRepository.save(message));
    }

    @Override
    public void delete(UUID messageId) {
        Message message = findEntity(messageId);
        for (UUID attachmentId : message.getAttachmentIds()) {
            binaryContentRepository.deleteById(attachmentId);
        }
        messageRepository.deleteById(messageId);
    }

    private Message findEntity(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new DiscodeitException(ExceptionType.MESSAGE_NOT_FOUND,
                        "메시지를 찾을 수 없습니다: " + messageId));
    }

    private MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannelId(),
                message.getAuthorId(),
                message.getAttachmentIds()
        );
    }
}
