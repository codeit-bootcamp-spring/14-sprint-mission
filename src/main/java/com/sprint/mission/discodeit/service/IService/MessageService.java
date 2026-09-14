package com.sprint.mission.discodeit.service.IService;


import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

    MessageResponseDto create(MessageCreateRequestDto messageRequest, List<BinaryContentCreateRequestDto> attachmentRequests);

    PageResponse<MessageResponseDto> findAllByChannelId(UUID channelId, Pageable pageable);   // ← 이렇게 바꾸기

    MessageResponseDto update(UUID id, MessageUpdateRequestDto request);

    void delete(UUID id);



}
