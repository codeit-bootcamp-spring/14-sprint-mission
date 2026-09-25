package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.adaptor.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.adaptor.dto.readStatus.ReadStatusResponse;
import com.sprint.mission.discodeit.adaptor.dto.readStatus.ReadStausRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusResponse create(ReadStatusCreateRequest readStatusCreateRequest);

  List<ReadStatusResponse> findByChannelId(UUID channelId);

  ReadStatusResponse findById(UUID uuid);

  List<ReadStatusResponse> findByUserId(UUID userId);

  void delete(UUID uuid);

  ReadStatusResponse update(ReadStausRequest readStausRequest, UUID readStatusId);
}
