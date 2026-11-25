package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends CrudRepository<ReadStatus, UUID> {
    List<ReadStatus> findAllEntityByChannelId(UUID channelId);
    List<ReadStatus> findAllReadStatusByUserId(UUID userId);
    void deleteReadStatusByChannelId(UUID channelId);
    Optional<ReadStatus> findReadStatusByUserIdAndChannelId(UUID userId, UUID channelId);
}
