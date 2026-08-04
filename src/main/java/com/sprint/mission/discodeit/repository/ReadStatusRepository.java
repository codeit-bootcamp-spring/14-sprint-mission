package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;

public interface ReadStatusRepository extends CrudRepository<ReadStatus> {

    List<ReadStatus> createAll(List<ReadStatus> readStatuses);
}
