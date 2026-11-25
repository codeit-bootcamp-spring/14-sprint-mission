package com.sprint.mission.discodeit.core.application.readstatus.port.out;

import com.sprint.mission.discodeit.core.common.persistence.CrudRepository;
import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusPort extends CrudRepository<ReadStatus, UUID> {
    List<ReadStatus> saveAll(List<ReadStatus> readStatuses);

    boolean existsByUserAndChannel(UUID userId, UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<UUID> findAllUserIdByChannelId(UUID channelId);

    void deleteAllByUserId(UUID userId);

    void deleteAllByChannelId(UUID channelId);
}
