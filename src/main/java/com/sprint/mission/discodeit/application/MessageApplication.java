package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.channel.ChannelType;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageApplication {
    private final UserService userService;
    private final MessageService messageService;
    private final ChannelService channelService;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;

    public MessageResponseDto createMessage(String content, UUID channelId, UUID userId,
                                            @Nullable List<MultipartFile> attachments) {
        userService.validateExistsById(userId);
        Channel channel = channelService.findById(channelId);

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
        Message created = messageService.create(message);
        return MessageResponseDto.of(created);
    }

    public Message getMessage(UUID id) {
        return messageService.findById(id);
    }

    public List<Message> getAllMessages() {
        return messageService.findAll();
    }

    public List<MessageResponseDto> getAllByChannelId(UUID channelId) {
        return messageService.findAllByChannelId(channelId).stream()
                .map(MessageResponseDto::of)
                .toList();
    }

    public MessageResponseDto updateMessage(UUID id, String content) {
        Message updated = messageService.updateContent(id, content);
        return MessageResponseDto.of(updated);
    }

    public MessageResponseDto deleteMessage(UUID id) {
        // binaryContent 필드에 다른 필드의 id가 없어서, 다른 엔티티에서 조회해야하는 번거로움
        Message toBeDeleted = messageService.findById(id);
        binaryContentRepository.delete(toBeDeleted.getAttachmentIds());
        Message deleted = messageService.deleteById(id);
        return MessageResponseDto.of(deleted);
    }
}
