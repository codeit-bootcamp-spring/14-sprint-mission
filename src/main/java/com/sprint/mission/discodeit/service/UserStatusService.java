package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResponse create(UserStatusRequest request);
    UserStatusResponse findUserStatus(UserStatusRequest request);
    List<UserStatusResponse> findAll();
    void delete(UserStatusRequest request);
    void updateByUserId(UUID userId);
}
