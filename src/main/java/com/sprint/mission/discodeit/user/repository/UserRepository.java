package com.sprint.mission.discodeit.user.repository;

import com.sprint.mission.discodeit.user.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(UUID id); // Optional 로 감싸기
    List<User> findAll();
    void deleteById(UUID id);

    Optional<User> findByUsername(String username);

    void update(User user);


    Optional<User> findByEmail(String email);
}
