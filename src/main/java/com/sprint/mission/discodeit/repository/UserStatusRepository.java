package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    Optional<UserStatus> read(UUID uuid);
    void delete(UUID uuid);
    List<UserStatus> readAll();
}
