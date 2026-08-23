package com.sprint.mission.discodeit.user.application;

import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatusResponseDto create(UserStatusCreateRequestDto request);

    UserStatusResponseDto find(UUID id);

    List<UserStatusResponseDto> findAll();

    UserStatusResponseDto update(UUID id);

    UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateRequestDto request);

    void delete(UUID id);
}
