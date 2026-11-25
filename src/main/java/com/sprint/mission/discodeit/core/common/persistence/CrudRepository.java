package com.sprint.mission.discodeit.core.common.persistence;

import com.sprint.mission.discodeit.core.domain.base.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T extends BaseEntity, I> {

    T save(T t);

    Optional<T> findById(I id);

    List<T> findAll();

    boolean existsById(I id);

    void deleteById(I id);

    void delete(T t);

    default T get(I id) {
        String errorMessage = String.format("Entity by id %s does not exists", id);
        return findById(id).orElseThrow(() -> new NotFoundException(errorMessage));
    }
}
