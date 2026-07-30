package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponseDto create(MessageCreateRequestDto requestDto);
    MessageResponseDto read(UUID id);
    List<MessageResponseDto> readAll();
    MessageResponseDto update(MessageUpdateRequestDto requestDto);
    void delete(UUID id);
}
