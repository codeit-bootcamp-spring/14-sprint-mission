package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.common.multipart.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageApplication {
    private final MessageService messageService;
    private final ChannelService channelService;
    private final MessageCreateService messageCreateService;

    @Transactional
    public MessageResponseDto createMessage(String content, UUID channelId, UUID userId,
                                            List<CreateBinaryContentCommand> createFileCommands) {
        Message created = messageCreateService.create(content, channelId, userId, createFileCommands);
        return MessageResponseDto.of(created);
    }

    @Transactional
    public List<MessageResponseDto> getAllByChannelId(UUID channelId) {
        channelService.validateExists(channelId);
        return messageService.findAllByChannelId(channelId).stream()
                .map(MessageResponseDto::of)
                .toList();
    }

    @Transactional
    public MessageResponseDto updateMessage(UUID id, String content) {
        Message updated = messageService.updateContent(id, content);
        return MessageResponseDto.of(updated);
    }

    @Transactional
    public MessageResponseDto deleteMessage(UUID id) {
        Message deleted = messageService.deleteById(id);
        return MessageResponseDto.of(deleted);
    }
}
