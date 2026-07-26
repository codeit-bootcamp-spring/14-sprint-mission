package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudRepository<T extends Entity> {
    T create(T t);

    Optional<T> findById(UUID id);

    List<T> findAll();

    void deleteById(UUID id);
}
