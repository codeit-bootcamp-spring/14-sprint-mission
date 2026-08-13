package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    List<User> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID id);

    // UserService 고도화
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // AuthService 구현을 위한 메서드
    Optional<User> findByUsernameAndPassword(String username, String password);
}
