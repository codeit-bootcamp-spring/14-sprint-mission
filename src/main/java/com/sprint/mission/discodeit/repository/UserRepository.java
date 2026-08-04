package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import jakarta.annotation.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User> {
    boolean existsById(UUID id);

    boolean existsByNameOrEmail(String name, String email);

    Optional<User> findByNameAndPassword(String name, String password);

    void update(UUID id, String name, String email, String password, @Nullable UUID profileId);
}
