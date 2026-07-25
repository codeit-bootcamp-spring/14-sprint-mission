package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User create(User user);

    boolean existsById(UUID id);

    Optional<User> findById(UUID id);

    List<User> findAll();

    void update(UUID id, String name);

    void deleteById(UUID id);
}
