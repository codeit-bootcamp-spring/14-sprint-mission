package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UpdatableEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Service<T extends UpdatableEntity> {

    T save(T entity);
    Optional<T> findById(UUID id);
    List<T> findAll();
    T update(T entity);
     void delete(UUID id);

    default String findIdAsString(UUID id) {
        return findById(id)
            .map(Object::toString)
            .orElse("해당 데이터를 찾을 수 없습니다. ID(" + id + ")");
    }



}

