package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.common.BasicEntity;
import com.sprint.mission.discodeit.repository.CrudRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public abstract class AbstractJCFRepository<T extends BasicEntity> implements CrudRepository<T> {
    protected final Map<UUID, T> STORE = new ConcurrentHashMap<>();

    @Override
    public T create(T t) {
        UUID id = t.getId();
        return findById(id).orElseGet(() -> {
            STORE.put(id, t);
            return t;
        });
    }

    @Override
    public Optional<T> findById(UUID id) {
        return Optional.ofNullable(STORE.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(STORE.values());
    }

    @Override
    public T deleteById(UUID id) {
        T deleted = STORE.get(id);
        STORE.remove(id);
        return deleted;
    }
}
