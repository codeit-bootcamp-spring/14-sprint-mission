package com.sprint.mission.discodeit.application.user.out;

import com.sprint.mission.discodeit.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends UserRepositoryCustom, JpaRepository<User, UUID> {
    boolean existsByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsernameAndPassword(String username, String password);

    boolean existsByUsernameAndIdNot(String username, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);
}
