package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

public interface Service<T> {

    void create(T entity);

    T read(UUID id);

    void delete(UUID id);

    List<T> findAll();
}
