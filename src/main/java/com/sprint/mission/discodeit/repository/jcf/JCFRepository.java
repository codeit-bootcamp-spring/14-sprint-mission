package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.repository.Repository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class JCFRepository<T extends BaseEntity> implements Repository<T> {
    private final Map<UUID,T> data;

    protected JCFRepository() {
        this.data = new LinkedHashMap<>(); // 입력된 값 순서대로 출력 보장
    }

    @Override
    public T save(T entity) {
        this.data.put(entity.getId(),entity);
        return entity;
    }

    @Override
    public Optional<T> findById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public List<T> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public T update(T entity) {
        this.data.put(entity.getId(),entity);
        return entity;
    }

    @Override
    public void delete(UUID id) {
        this.data.remove(id);

    }
}
