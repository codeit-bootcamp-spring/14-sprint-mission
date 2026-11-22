package com.sprint.mission.discodeit.adapter.out.persistence.user;

import com.sprint.mission.discodeit.core.domain.user.Role;
import com.sprint.mission.discodeit.core.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository
        extends JpaRepository<User, UUID>, UserQueryRepository {
    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByRole(Role role);
}
