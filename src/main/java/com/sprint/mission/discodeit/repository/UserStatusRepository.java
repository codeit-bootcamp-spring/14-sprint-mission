package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.userstatus.UserStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends CrudRepository<UserStatus> {
    Optional<UserStatus> findByUserId(UUID userId);

    void deleteByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    UserStatus updateLastActiveAtByUserId(UUID userId, Instant newLastActiveAt);
}
