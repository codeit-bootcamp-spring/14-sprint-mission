package com.sprint.mission.application.message;

import com.sprint.mission.controller.dto.message.MessageCreateRequestDto;
import com.sprint.mission.controller.dto.message.MessageResponseDto;
import com.sprint.mission.controller.dto.message.MessageUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageApplicationService {
    MessageResponseDto create(
            MessageCreateRequestDto messageCreateRequest,
            List<MultipartFile> attachment
    );
    MessageResponseDto findById(UUID messageId);
    List<MessageResponseDto> findAllByChannelId(UUID channelId);
    MessageResponseDto update(UUID messageId, MessageUpdateRequestDto messageUpdateRequest);
    void delete(UUID messageId);
}
