package com.sprint.mission.discodeit.user.application;

import com.sprint.mission.discodeit.readStatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatusDto create(UserStatusCreateRequestDto request);

    UserStatusDto find(UUID id);

    List<UserStatusDto> findAll();

    UserStatusDto update(UUID id);

    UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequestDto request);

    void delete(UUID id);
}
