package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatusdto.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatusdto.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userstatusdto.UserStatusUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatusResponseDto createUserStatus(UserStatusCreateRequestDto requestDto);

    UserStatusResponseDto readUserStatus(UUID id);

    List<UserStatusResponseDto> readAllUserStatus();

    UserStatusResponseDto updateUserStatus(UUID id, UserStatusUpdateRequestDto requestDto);

    UserStatusResponseDto updateUserStatusByUserId(UUID userId, UserStatusUpdateRequestDto requestDto);

    void deleteUserStatus(UUID id);
}
