package com.sprint.mission.discodeit.repository.readstatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepositoryCustom {
    List<UUID> findAllUserIdByChannelId(UUID channelId);
}
