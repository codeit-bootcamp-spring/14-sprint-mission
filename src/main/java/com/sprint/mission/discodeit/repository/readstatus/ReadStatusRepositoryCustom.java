package com.sprint.mission.discodeit.repository.readstatus;

import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepositoryCustom {
    List<UUID> findAllUserIdByChannelId(UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);
}
