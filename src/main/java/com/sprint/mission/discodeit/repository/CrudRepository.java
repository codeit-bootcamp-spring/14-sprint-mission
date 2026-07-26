package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudRepository<T> {
    T create(T t);

    Optional<T> findById(UUID id);

    List<T> findAll();

    void deleteById(UUID id);
}
