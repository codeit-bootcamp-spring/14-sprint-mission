package com.sprint.mission.discodeit.adapter.out.persistence.repository.readstatus;

import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusQueryRepository {
    List<UUID> findAllUserIdByChannelId(UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);
}
