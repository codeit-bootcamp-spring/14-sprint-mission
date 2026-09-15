package com.sprint.mission.discodeit.message.application;

import com.sprint.mission.discodeit.common.application.BasicService;
import com.sprint.mission.discodeit.common.dto.PageResponse;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService extends BasicService<Message> {
    MessageDto create(MessageCreateRequestDto request, List<MultipartFile>  attachments);

    MessageDto find(UUID id);

    MessageDto update(UUID id, MessageUpdateRequestDto request);

    PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable);
}
