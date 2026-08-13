package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    Optional<UserStatus> findById(UUID id);
    List<UserStatus> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID id);

    // UserService 고도화
    Optional<UserStatus> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);

    // userStatusService 고도화
    boolean existsByUserId(UUID userId);
}
