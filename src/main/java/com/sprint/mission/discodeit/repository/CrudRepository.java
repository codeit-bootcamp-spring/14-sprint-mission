package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.common.BasicEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudRepository<T extends BasicEntity> {
    T create(T t);

    Optional<T> findById(UUID id);

    List<T> findAll();

    T deleteById(UUID id);

    boolean existsById(UUID id);
}
