package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends CrudRepository<ReadStatus> {
    boolean existsByUserAndChannel(UUID userId, UUID channelId);

    List<ReadStatus> createAll(List<ReadStatus> readStatuses);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    void deleteByChannelId(UUID channelId);

    void update(UUID id);

    void deleteByUserId(UUID userId);

    List<UUID> findAllUserIdsByChannelId(UUID channelId);
}
