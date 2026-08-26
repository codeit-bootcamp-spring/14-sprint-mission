package com.sprint.mission.discodeit.message.application;

import com.sprint.mission.discodeit.common.application.BasicService;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService extends BasicService<Message> {
    MessageResponseDto create(MessageCreateRequestDto request, List<MultipartFile>  attachments);

    MessageResponseDto find(UUID id);

    MessageResponseDto update(UUID id, MessageUpdateRequestDto request);

    List<MessageResponseDto> findAllByChannelId(UUID channelId);
}
