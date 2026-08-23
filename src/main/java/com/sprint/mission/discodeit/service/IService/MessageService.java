package com.sprint.mission.discodeit.service.IService;


import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResponseDto create(MessageCreateRequestDto request, List<BinaryContentCreateRequestDto> attachmentRequests);
    List<MessageResponseDto> findAllByChannelId(UUID channelId);
    MessageResponseDto update(UUID id, MessageUpdateRequestDto request);
    void delete(UUID id);



}
