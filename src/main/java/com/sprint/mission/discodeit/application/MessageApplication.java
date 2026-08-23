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
    private final MessageService messageService;
    private final ChannelService channelService;
    private final BinaryContentService binaryContentService;
    private final MessageCreateService messageCreateService;

    public MessageResponseDto createMessage(String content, UUID channelId, UUID userId,
                                            List<CreateBinaryContentCommand> createFileCommands) {
        Message created = messageCreateService.create(content, channelId, userId, createFileCommands);
        return MessageResponseDto.of(created);
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
