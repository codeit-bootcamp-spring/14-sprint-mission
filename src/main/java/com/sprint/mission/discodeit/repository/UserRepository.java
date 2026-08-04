package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import jakarta.annotation.Nullable;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User> {
    boolean existsById(UUID id);

    boolean existsByNameOrEmail(String name, String email);

    void update(UUID id, String name, String email, String password, @Nullable UUID profileId);
}
