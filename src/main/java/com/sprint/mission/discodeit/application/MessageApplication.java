package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.common.multipart.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageApplication {
    private final UserService userService;
    private final MessageService messageService;
    private final ChannelService channelService;
    private final BinaryContentService binaryContentService;
    private final ReadStatusService readStatusService;

    public MessageResponseDto createMessage(String content, UUID channelId, UUID userId,
                                            List<CreateBinaryContentCommand> createFileCommands) {
        userService.validateExistsById(userId);
        Channel channel = channelService.findById(channelId);

        // PRIVATE 채널의 경우 소속된 User만 Message 생성 가능
        // TODO 비즈니스 규칙
        if (channel.isPrivate() && !readStatusService.existsByUserAndChannel(userId, channelId)) {
            throw new CustomException(ExceptionType.NO_ACCESS_TO_CHANNEL);
        }

        List<UUID> createdAttachmentIds = createFilesAndThenGetIdIfNotNullOrElseGetNull(createFileCommands);

        Message message = new Message(content, userId, channelId, createdAttachmentIds);
        Message created = messageService.create(message);
        channelService.update(channel.getId(), created.getCreatedAt());
        return MessageResponseDto.of(created);
    }

    private @Nullable List<UUID> createFilesAndThenGetIdIfNotNullOrElseGetNull(List<CreateBinaryContentCommand> createFileCommands) {
        return Objects.nonNull(createFileCommands) ?
                createFileCommands.stream()
                        .map(command -> {
                            return binaryContentService.create(BinaryContent.of(
                                    command.fileName(),
                                    command.contentType(),
                                    command.content()
                            )).getId();
                        }).toList()
                : null;

    }

    public Message getMessage(UUID id) {
        return messageService.findById(id);
    }

    public List<Message> getAllMessages() {
        return messageService.findAll();
    }

    public List<MessageResponseDto> getAllByChannelId(UUID channelId) {
        channelService.validateExists(channelId);
        return messageService.findAllByChannelId(channelId).stream()
                .map(MessageResponseDto::of)
                .toList();
    }

    public MessageResponseDto updateMessage(UUID id, String content) {
        Message updated = messageService.updateContent(id, content);
        return MessageResponseDto.of(updated);
    }

    public MessageResponseDto deleteMessage(UUID id) {
        Message toBeDeleted = messageService.findById(id);
        binaryContentService.deleteById(toBeDeleted.getAttachmentIds());
        Message deleted = messageService.deleteById(id);
        return MessageResponseDto.of(deleted);
    }
}
