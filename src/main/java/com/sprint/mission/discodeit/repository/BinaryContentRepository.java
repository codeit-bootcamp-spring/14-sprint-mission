package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository extends CrudRepository<BinaryContent> {
    void delete(List<UUID> ids);
    List<BinaryContent> findAllById(List<UUID> ids);
}
