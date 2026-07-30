package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// T는 엔티티 타입(User, Channel, Message 등), ID는 보통 UUID를 사용합니다.
public interface FileRepository<T> {

    T save(T entity);
    Optional<T> findById(UUID id); // 또는 null을 반환하는 T findById(UUID id)
    List<T> findAll();
    void deleteById(UUID id);
    void deleteAll();
}