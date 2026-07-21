package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User userAdd(User user);
    Optional<User> findByUser(String userName);
    void delete(User user);
    List<User> findAllUser();
}
