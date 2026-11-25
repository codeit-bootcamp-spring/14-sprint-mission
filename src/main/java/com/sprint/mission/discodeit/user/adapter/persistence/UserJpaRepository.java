package com.sprint.mission.discodeit.user.adapter.persistence;

import com.sprint.mission.discodeit.user.domain.Email;
import com.sprint.mission.discodeit.user.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(Email email);
}
