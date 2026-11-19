package com.sprint.mission.discodeit.core.application.readstatus.out;

import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepositoryCustom {
    List<UUID> findAllUserIdByChannelId(UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);
}
