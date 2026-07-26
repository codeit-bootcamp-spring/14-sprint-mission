package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.service.BasicService;

import java.util.*;

public abstract class JCFBasicService<T extends BaseEntity> implements BasicService<T> {
    private final Map<UUID, T> data;

    public JCFBasicService() {
        this.data = new HashMap<>();
    }

    @Override
    public T read(UUID id) {
        check(id);
        return data.get(id);
    }

    @Override
    public void update(T t) {
        check(t.getId());

        data.put(t.getId(), t);
    }

    @Override
    public void delete(UUID id) {
        check(id);

        data.remove(id);
    }

    @Override
    public List<T> readAll() {
        return new ArrayList<>(data.values());
    }


    public void check(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("해당 값이 존재하지 않습니다.");
        }
    }
}
