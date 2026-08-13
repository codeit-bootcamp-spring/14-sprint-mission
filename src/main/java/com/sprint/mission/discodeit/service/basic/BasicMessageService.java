package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AttachmentRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    MessageRepository messageRepository;
    ChannelRepository channelRepository;
    UserRepository userRepository;
    BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponse create(MessageCreateRequest request) {
        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("채널을 찾을 수 없습니다.");
        }
        if (!userRepository.existsById(request.authorId())) {
            throw new NoSuchElementException("유저를 찾을 수 없습니다.");
        }

        Message message = new Message(request.content(), request.channelId(), request.authorId());

        if (request.attachments() != null && !request.attachments().isEmpty()){
            for (AttachmentRequest attachment : request.attachments()) {
                BinaryContent binaryContent = new BinaryContent(
                        attachment.bytes(),
                        attachment.fileName(),
                        attachment.contentType()
                );
                binaryContentRepository.save(binaryContent);
                message.addAttachment(binaryContent.getId());
            }
        }

        messageRepository.save(message);
        return toResponse(message);
    }

    @Override
    public MessageResponse find(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메세지를 찾을 수 없습니다."));
        return toResponse(message);
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        if (!channelRepository.existsById(channelId)){
            throw new NoSuchElementException("채널을 찾을 수 없습니다.");
        }
        return messageRepository.findByChannelId(channelId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MessageResponse update(MessageUpdateRequest request) {
        Message message = messageRepository.findById(request.messageId())
                .orElseThrow(() -> new NoSuchElementException("메세지를 찾을 수 없습니다."));
        message.update(request.newContent());
        messageRepository.save(message);
        return toResponse(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메세지를 찾을 수 없습니다."));

        if (message.getAttachmentIds() != null) {
            for (UUID attachmentId : message.getAttachmentIds()) {
                binaryContentRepository.deleteById(attachmentId);
            }
        }
        messageRepository.deleteById(messageId);
    }

    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getChannelId(),
                message.getAuthorId(),
                message.getAttachmentIds(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
