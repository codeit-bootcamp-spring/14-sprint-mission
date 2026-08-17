package com.sprint.mission.discodeit.readStatus.application;

import com.sprint.mission.discodeit.readStatus.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusUpdateResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponseDto create(ReadStatusCreateRequestDto request);

    ReadStatusResponseDto find(UUID id);

    List<ReadStatusResponseDto> findAllByUserId(UUID userId);

    ReadStatusUpdateResponseDto update(UUID id);

    void delete(UUID id);
}
