package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.BinaryContentCtreateRequestDto;
import com.sprint.mission.discodeit.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.MessageResponseDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequestDto;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResponseDto create(MessageCreateRequestDto request, List<BinaryContentCreateRequestDto> attachmentRequests);
    List<MessageResponseDto> findAllByChaanelId(UUID channelId);
    MessageResponseDto update(UUID id, MessageUpdateRequestDto request);
    void delete(UUID id);



}
