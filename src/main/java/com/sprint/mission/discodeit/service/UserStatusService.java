package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatusDto find(UUID userStatusId);

    List<UserStatusDto> findAll();

    UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request);

    UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request);
}
