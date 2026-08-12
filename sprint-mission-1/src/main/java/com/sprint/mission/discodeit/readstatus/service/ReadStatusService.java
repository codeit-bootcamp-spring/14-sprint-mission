package com.sprint.mission.discodeit.readstatus.service;

import com.sprint.mission.discodeit.readstatus.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusUpdateRequestDto;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusResponseDto readStatusCreate(ReadStatusCreateRequestDto readStatusCreateRequestDto);

    ReadStatusResponseDto readStatusUpdate(UUID readStatusId,
        ReadStatusUpdateRequestDto readStatusUpdateRequestDto);

    void readStatusDelete(UUID readStatusId);

    List<ReadStatusResponseDto> findAllByUserId(UUID userId);

    ReadStatusResponseDto findReadStatus(UUID readStatusId);
}
