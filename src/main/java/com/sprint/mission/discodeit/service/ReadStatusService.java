package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStausRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusResponse create(ReadStatusCreateRequest readStatusCreateRequest);

    List<ReadStatusResponse> findByChannelId(UUID channelId);

    ReadStatusResponse findById(UUID uuid);
    List<ReadStatusResponse> findByUserId(UUID userId);
    void delete(UUID uuid);
    ReadStatusResponse update(ReadStausRequest readStausRequest);
}
