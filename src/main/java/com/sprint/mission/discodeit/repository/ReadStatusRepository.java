package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends CrudRepository<ReadStatus> {

    List<ReadStatus> createAll(List<ReadStatus> readStatuses);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    void deleteByChannelId(UUID channelId);
}
