package com.sprint.mission.discodeit.user.adapter.persistence;

import com.sprint.mission.discodeit.user.domain.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusJpaRepository extends JpaRepository<UserStatus, UUID> {

  Optional<UserStatus> findByUser_Id(UUID userId);
}
