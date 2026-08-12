package com.sprint.mission.discodeit.userstatus.repository;

import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {

    UserStatus statusAdd(UserStatus userStatus);

    void delete(UserStatus userStatus);

    void update(UserStatus userStatus);

    UserStatus findById(UUID userStatusId);

    Optional<UserStatus> findByUserId(UUID userId);

    List<UserStatus> findAll();
}
