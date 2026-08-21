package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.ChannelType;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.*;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;

    public MessageResponseDto createMessage(String content, UUID channelId, UUID userId,
                                            @Nullable List<MultipartFile> attachments) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE);
        }

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ExceptionType.CHANNEL_NOT_FOUND_IN_DATABASE));

        // PRIVATE 채널의 경우 소속된 User만 Message 생성 가능
        if (channel.getChannelType().equals(ChannelType.PRIVATE) && !readStatusRepository.existsByUserAndChannel(userId, channelId)) {
            throw new CustomException(ExceptionType.NO_ACCESS_TO_CHANNEL);
        }

        List<UUID> createdAttachmentIds = null;
        if (Objects.nonNull(attachments)) {
            createdAttachmentIds = attachments.stream()
                    .map(eachAttachment -> {
                        BinaryContent createdAttachment = new BinaryContent(eachAttachment);
                        return binaryContentRepository.create(createdAttachment).getId();
                    })
                    .toList();
        }

        Message message = new Message(content, userId, channelId, createdAttachmentIds);
        Message created = messageRepository.create(message);
        return MessageResponseDto.of(created);
    }

    public Optional<Message> getMessage(UUID id) {
        return messageRepository.findById(id);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<MessageResponseDto> getAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .map(MessageResponseDto::of)
                .toList();
    }

    // TODO 구현은 나중에, 앤티티 수정해야 해서 너무 오래 걸릴 듯,,
    public MessageResponseDto updateMessage(UUID id, String content) {
        Message updated = messageRepository.updateContent(id, content);
        return MessageResponseDto.of(updated);
    }

    public MessageResponseDto deleteMessage(UUID id) {
        // binaryContent 필드에 다른 필드의 id가 없어서, 다른 엔티티에서 조회해야하는 번거로움
        Message toBeDeleted = messageRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.MESSAGE_NOT_FOUND_IN_DATABASE));
        binaryContentRepository.delete(toBeDeleted.getAttachmentIds());

        Message deleted = messageRepository.deleteById(id);
        return MessageResponseDto.of(deleted);
    }
}
