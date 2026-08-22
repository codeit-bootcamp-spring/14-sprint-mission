package com.sprint.mission.discodeit.userstatus.service;

import com.sprint.mission.discodeit.userstatus.dto.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusResponseDto;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusUpdateRequestDto;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatusResponseDto userStatusCreate(UserStatusCreateRequestDto userStatusCreateRequestDto);

    UserStatusResponseDto userStatusUpdate(UUID userStatusId,
        UserStatusUpdateRequestDto userStatusUpdateRequestDto);

    UserStatusResponseDto userStatusUpdateByUserId(UUID userId,
        UserStatusUpdateRequestDto userStatusUpdateRequestDto);

    void userStatusDelete(UUID userStatusId);

    List<UserStatusResponseDto> findAllByUserId(UUID userId);

    UserStatusResponseDto findUserStatus(UUID userStatusId);
}
