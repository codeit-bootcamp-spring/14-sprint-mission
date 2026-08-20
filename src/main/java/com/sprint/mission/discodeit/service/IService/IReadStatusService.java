package com.sprint.mission.discodeit.service.IService;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequestDto;
import java.util.List;
import java.util.UUID;

public interface IReadStatusService {
    ReadStatusResponseDto create(ReadStatusCreateRequestDto request);
    ReadStatusResponseDto find(UUID id);
    List<ReadStatusResponseDto> findAllByUserId(UUID id);
    ReadStatusResponseDto update(UUID id, ReadStatusUpdateRequestDto request);
    void delete(UUID id);


}
