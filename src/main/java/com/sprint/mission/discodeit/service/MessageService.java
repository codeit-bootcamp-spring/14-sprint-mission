package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResponseDto create(MessageCreateRequestDto request);
    List<MessageResponseDto> findAllByChaanelId(UUID id);
    MessageResponseDto update(UUID id, MessageUpdateRequestDto request);
    void delete(UUID id);



}
