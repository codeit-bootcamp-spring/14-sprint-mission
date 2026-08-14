package com.sprint.mission.discodeit.service.application.message;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
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
