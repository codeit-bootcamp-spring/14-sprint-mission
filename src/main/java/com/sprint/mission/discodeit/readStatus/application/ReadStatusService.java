package com.sprint.mission.discodeit.readStatus.application;

import com.sprint.mission.discodeit.readStatus.dto.*;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDto create(ReadStatusCreateRequestDto request);

    ReadStatusDto find(UUID id);

    List<ReadStatusDto> findAllByUserId(UUID userId);

    ReadStatusDto update(UUID id, ReadStatusUpdateRequestDto request);

    void delete(UUID id);
}
