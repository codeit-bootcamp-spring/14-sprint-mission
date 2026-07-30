package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(String name, String email, String nickname);
    Optional<User> read(UUID id);
    List<User> readAll();
    void update(UUID id, String name, String email, String nickname);
    void delete(UUID id);
}
