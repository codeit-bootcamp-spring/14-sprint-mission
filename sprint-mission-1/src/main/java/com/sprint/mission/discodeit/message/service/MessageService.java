package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResponseDto messageCreate(MessageCreateRequestDto messageCreateRequestDto);

    MessageResponseDto messageUpdate(UUID messageId,
        MessageUpdateRequestDto messageUpdateRequestDto);

    void messageDelete(UUID messageId);

    List<MessageResponseDto> findAllByChannelId(UUID channelId);

    MessageResponseDto findById(UUID messageId);
}
