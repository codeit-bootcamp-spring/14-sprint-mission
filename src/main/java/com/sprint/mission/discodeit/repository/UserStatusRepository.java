package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    Optional<UserStatus> findById(UUID id);
    /** 유저 하나에 상태 하나라 List가 아니라 Optional이다. */
    Optional<UserStatus> findByUserId(UUID userId);
    List<UserStatus> findAll();
    void deleteById(UUID id);
    void deleteByUserId(UUID userId);
}
