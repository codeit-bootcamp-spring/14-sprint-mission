package com.sprint.mission.discodeit.core.application.user.out;

import com.sprint.mission.discodeit.core.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface UserRepositoryCustom {
    List<User> findByIdIn(List<UUID> userIds);

    List<User> findAll();
}
