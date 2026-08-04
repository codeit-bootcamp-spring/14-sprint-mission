package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.messagedto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageResponseDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    // Create
    MessageResponseDto createMessage(MessageCreateRequestDto requestDto);

    // Read (id를 받아 MessageResponseDto 반환)
    MessageResponseDto readMessage(UUID id);

    // Read (전체 조회)
    List<MessageResponseDto> readAllMessage();

    // Update (메시지 내용 변경)
    MessageResponseDto updateMessage(UUID id, MessageUpdateRequestDto requestDto);

    // Delete (메시지 삭제)
    void deleteMessage(UUID id);
}
