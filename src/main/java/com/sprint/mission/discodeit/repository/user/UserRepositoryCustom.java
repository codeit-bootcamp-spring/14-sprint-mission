package com.sprint.mission.discodeit.repository.user;

import com.sprint.mission.discodeit.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface UserRepositoryCustom {
    List<User> findByIdIn(List<UUID> userIds);

    List<User> findAll();
}
