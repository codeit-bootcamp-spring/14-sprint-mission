package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.user.User;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByName(String name);

    boolean existsAllByIds(List<UUID> ids);

    boolean existsByNameOrEmail(String name, String email);

    Optional<User> findByNameAndPassword(String name, String password);

    User update(@Nullable UUID id, @Nullable String name, @Nullable String email, @Nullable String password, @Nullable UUID profileId);

    List<User> findByIds(List<UUID> userIds);
}
