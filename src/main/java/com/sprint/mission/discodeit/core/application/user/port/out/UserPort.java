package com.sprint.mission.discodeit.core.application.user.port.out;

import com.sprint.mission.discodeit.core.common.persistence.CrudRepository;
import com.sprint.mission.discodeit.core.domain.user.Role;
import com.sprint.mission.discodeit.core.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface UserPort extends CrudRepository<User, UUID> {
    boolean existsByUsernameOrEmail(String username, String email);

    User get(String username);

    boolean existsByUsernameAndIdNot(String username, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByRole(Role role);

    List<User> findByIdIn(List<UUID> userIds);
}
