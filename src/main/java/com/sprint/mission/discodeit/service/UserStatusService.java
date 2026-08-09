package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatusResponseDto create(UserStatusCreateRequestDto request);

    UserStatusResponseDto find(UUID id);

    List<UserStatusResponseDto> findAll();

    UserStatusResponseDto update(UserStatusUpdateRequestDto request);

    UserStatusResponseDto updateByUserId(UUID userId);

    void delete(UserStatusUpdateRequestDto request);
}
