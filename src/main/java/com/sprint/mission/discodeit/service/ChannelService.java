package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channeldto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.PrivateChannelCreateRequestDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelResponseDto createPublicChannel(ChannelCreateRequestDto requestDto);

    ChannelResponseDto createChannel(ChannelCreateRequestDto requestDto);

    ChannelResponseDto createPrivateChannel(PrivateChannelCreateRequestDto requestDto);

    ChannelResponseDto readChannel(UUID id);

    List<ChannelResponseDto> readAllChannel();

    List<ChannelResponseDto> findAllByUserId(UUID userId);

    ChannelResponseDto updateChannel(UUID id, ChannelUpdateRequestDto requestDto);

    void deleteChannel(UUID id);
}
