package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsernameAndPassword(String username, String password);

    List<User> findByIdIn(List<UUID> userIds);

    boolean existsByUsernameAndIdNot(String username, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);
}
