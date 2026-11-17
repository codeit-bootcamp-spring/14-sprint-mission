package com.sprint.mission.discodeit.application.user.out;

import com.sprint.mission.discodeit.domain.user.Role;
import com.sprint.mission.discodeit.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends UserRepositoryCustom, JpaRepository<User, UUID> {
    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByRole(Role role);
}
