package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(@Valid ReadStatusCreateDto dto);

    ReadStatus getReadStatus(UUID id);

    List<ReadStatus> getAllReadStatusByUserId(UUID userId);

    void updateReadStatus(UUID id);

    void deleteReadStatus(UUID id);
}
