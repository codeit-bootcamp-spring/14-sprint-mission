package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {
    Optional<UserStatus> findByUserId(UUID userId);

    UserStatus deleteByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    UserStatus updateLastActiveAtByUserId(UUID userId, Instant newLastActiveAt);
}
