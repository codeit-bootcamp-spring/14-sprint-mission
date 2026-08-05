package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateDto dto);

    UserStatus getUserStatus(UUID id);

    List<UserStatus> getAllUserStatus();

    void update(UUID id);

    void updateByUserId(UUID userId);

    void delete(UUID id);
}
