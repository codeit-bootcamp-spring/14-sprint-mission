package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

public interface BasicService<T> {

    T read(UUID id);
    void update(T t);
    void delete(UUID id);
    List<T> readAll();
}
