package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    void save(UserStatus userStatus);
    Optional<UserStatus> findByUserId(UUID userId);
    void deleteById(UUID id);

    boolean existsByUserId(UUID userId);
    UserStatus findById(UUID id);

    List<UserStatus> findAll();
}
