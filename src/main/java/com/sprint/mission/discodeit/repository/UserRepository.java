package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.user.User;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User> {
    boolean existsById(UUID id);

    boolean existsAllByIds(List<UUID> ids);

    boolean existsByNameOrEmail(String name, String email);

    Optional<User> findByNameAndPassword(String name, String password);

    void update(UUID id, String name, String email, String password, @Nullable UUID profileId);
}
