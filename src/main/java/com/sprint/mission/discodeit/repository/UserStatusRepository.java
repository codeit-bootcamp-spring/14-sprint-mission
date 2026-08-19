package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.userstatus.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends CrudRepository<UserStatus> {
    Optional<UserStatus> findByUserId(UUID userId);

    void deleteByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    void update(UUID id);

    void updateByUserId(UUID userId);
}
