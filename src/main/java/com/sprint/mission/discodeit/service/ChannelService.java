package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponseDto create(ChannelCreateRequestDto requestDto);
    ChannelResponseDto read(UUID id);
    List<ChannelResponseDto> readAll();
    ChannelResponseDto update(ChannelUpdateRequestDto requestDto);
    void delete(UUID id);
}
