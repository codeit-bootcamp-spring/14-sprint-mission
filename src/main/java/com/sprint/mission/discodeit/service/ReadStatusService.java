package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponseDto create(ReadStatusCreateRequestDto request);

    ReadStatusResponseDto find(UUID id);

    List<ReadStatusResponseDto> findAllByUserId(UUID userId);

    ReadStatusResponseDto update(ReadStatusUpdateRequestDto request);

    void delete(UUID id);
}
