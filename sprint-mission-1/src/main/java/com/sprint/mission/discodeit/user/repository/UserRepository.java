package com.sprint.mission.discodeit.user.repository;

import com.sprint.mission.discodeit.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User userAdd(User user);

    Optional<User> findByUser(UUID userId);

    Optional<User> findByUserName(String name);

    Optional<User> findByUserEmail(String email);

    void delete(User user);

    void update(User user);

    List<User> findAllUser();
}
