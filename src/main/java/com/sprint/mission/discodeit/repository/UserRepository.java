package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.user.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User create(User user);

    Optional<User> findById(UUID id);

    List<User> findAll();

    void update(UUID id, UserDto dto);

    void deleteById(UUID id);
}
