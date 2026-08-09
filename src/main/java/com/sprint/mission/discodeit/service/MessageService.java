package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService extends BasicService<Message>{
    MessageResponseDto create(MessageCreateRequestDto request);

    MessageResponseDto find(UUID id);

    void update(MessageUpdateRequestDto request);

    List<MessageResponseDto> findAllByChannelId(UUID channelId);
}
