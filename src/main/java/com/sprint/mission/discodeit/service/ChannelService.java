package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channeldto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // Create (채널 생성)
    ChannelResponseDto createChannel(ChannelCreateRequestDto requestDto);

    // Read (채널 조회)
    ChannelResponseDto readChannel(UUID id);

    // Read (전체 채널 목록 조회)
    List<ChannelResponseDto> readAllChannel();

    // Update (채널 정보 변경)
    ChannelResponseDto updateChannel(UUID id, ChannelUpdateRequestDto requestDto);

    // Delete (채널 삭제)
    void deleteChannel(UUID id);
}
