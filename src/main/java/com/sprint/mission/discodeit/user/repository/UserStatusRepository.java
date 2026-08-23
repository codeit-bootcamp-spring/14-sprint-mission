package com.sprint.mission.discodeit.user.repository;

import com.sprint.mission.discodeit.user.domain.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {

    void save(UserStatus userStatus);

    void deleteByUserId(UUID userId);

    Optional<UserStatus> findById(UUID id);

    Optional<UserStatus> findByUserId(UUID userId);

    List<UserStatus> findAll();

    void deleteById(UUID id);

    Optional<UserStatus> update(UserStatus userStatus);

}
