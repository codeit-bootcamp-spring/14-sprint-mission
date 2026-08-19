package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existAllById(List<UUID> idList);
}
