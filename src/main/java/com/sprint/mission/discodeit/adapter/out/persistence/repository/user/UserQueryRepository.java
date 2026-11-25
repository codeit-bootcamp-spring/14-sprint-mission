package com.sprint.mission.discodeit.adapter.out.persistence.repository.user;

import com.sprint.mission.discodeit.core.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface UserQueryRepository {
    List<User> findByIdIn(List<UUID> userIds);

    List<User> findAll();
}
