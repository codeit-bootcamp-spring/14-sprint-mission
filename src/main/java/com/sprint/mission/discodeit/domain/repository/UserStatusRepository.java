package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.entity.UserStatus;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends CrudRepository<UserStatus, UUID> {
    Optional<UserStatus> findUserStatusByUserId(UUID userId);
}
