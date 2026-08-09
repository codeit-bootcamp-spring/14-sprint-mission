package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatusdto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatusdto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatusdto.ReadStatusUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusResponseDto createReadStatus(ReadStatusCreateRequestDto requestDto);

    ReadStatusResponseDto readReadStatus(UUID id);

    List<ReadStatusResponseDto> findAllByUserId(UUID userId);

    ReadStatusResponseDto updateReadStatus(UUID id, ReadStatusUpdateRequestDto requestDto);

    void deleteReadStatus(UUID id);
}
