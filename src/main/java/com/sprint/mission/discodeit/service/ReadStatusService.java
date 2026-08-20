package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponseDto create(ReadStatusCreateRequestDto request);
    ReadStatusResponseDto find(UUID id);
    List<ReadStausResponseDto> findAllByUserId(UUID id);
    ReadStatusResponseDto update(UUID id, ReadStatusUpdateRequestDto request);
    void delete(UUID id);


}
