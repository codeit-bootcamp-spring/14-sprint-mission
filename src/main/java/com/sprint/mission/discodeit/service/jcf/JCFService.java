package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.BasicEntity;
import com.sprint.mission.discodeit.service.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public abstract class JCFService<T extends BasicEntity> implements Service<T> {

    protected final Map<UUID, T> entityFile;

    public JCFService() {
        entityFile = new HashMap<>();
    }

    @Override
    public void create(T entity) {
        if (entityFile.containsKey(entity.getId())) {
            throw new RuntimeException("이미 존재하는 유저입니다: " + entity.getId());
        }
        entityFile.put(entity.getId(), entity);
    }

    @Override
    public T read(UUID id) {
        T entity = entityFile.get(id);
        if (Objects.isNull(entity)) {
            throw new RuntimeException("존재하지 않는 유저입니다: " + id);
        }

        return entity;
    }


    @Override
    public void delete(UUID id) {
        read(id);
        entityFile.remove(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entityFile.values());
    }

}
