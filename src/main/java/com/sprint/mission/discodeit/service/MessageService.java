package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Message;
import java.util.List;

public interface MessageService {

    MessageResponseDto create(MessageCreateRequestDto request);
    List<MessageResponseDto> findAllByChaanelId(Integer id);
    MessageResponseDto update(Integer id, MessageUpdateRequestDto request);
    void delete(Integer id);



}
