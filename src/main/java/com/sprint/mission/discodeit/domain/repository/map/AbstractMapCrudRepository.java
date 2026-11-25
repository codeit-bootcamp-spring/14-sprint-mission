package com.sprint.mission.discodeit.domain.repository.map;

import com.sprint.mission.discodeit.domain.entity.IdMapper;
import com.sprint.mission.discodeit.domain.repository.CrudRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public abstract class AbstractMapCrudRepository<T extends IdMapper> implements CrudRepository<T, UUID> {
    private final Map<UUID, T> entityList = new ConcurrentHashMap<>();

    @Override
    public T saveEntity(T entity) {
        entityList.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(UUID id) {
        return Optional.ofNullable(entityList.get(id));
    }

    @Override
    public void deleteEntity(UUID id) {
        entityList.remove(id);
    }


    @Override
    public List<T> findAllEntity() {
        return new ArrayList<>(entityList.values());
    }
}
