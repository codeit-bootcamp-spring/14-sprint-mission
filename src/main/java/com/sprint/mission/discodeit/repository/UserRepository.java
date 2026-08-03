package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User> {
    boolean existsById(UUID id);

    boolean existsByNameOrEmail(String name, String email);

    void updateName(UUID id, String name);
}
