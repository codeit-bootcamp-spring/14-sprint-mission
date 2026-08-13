package com.sprint.mission.discodeit.service.application.readstatus;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusApplicationService {
    ReadStatusResponseDto create(
            ReadStatusCreateRequestDto readStatusCreateRequest
    );
    ReadStatusResponseDto findById(UUID readStatusId);
    List<ReadStatusResponseDto> findAllByUserId(UUID userId);
    ReadStatusResponseDto update(UUID readStatusId);
    void delete(UUID readStatusId);
}
