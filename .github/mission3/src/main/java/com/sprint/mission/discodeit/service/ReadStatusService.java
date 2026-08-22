package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStausRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStausResponse;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStausResponse create(ReadStatusCreateRequest readStatusCreateRequest);

    ReadStausResponse findById(UUID uuid);
    List<ReadStausResponse> findByUserId(UUID userId);
    void delete(UUID uuid);
    ReadStausResponse update(ReadStausRequest readStausRequest);
}
