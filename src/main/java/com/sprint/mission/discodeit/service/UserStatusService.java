package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateDto;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResponseDto create(UserStatusCreateRequestDto request);
    UserStatusResponseDto find(UUID id);
    List<UserStatusResponseDto> findAll();
    UserStatusResponseDto update(UUID id, UserStatusUpdateDto request);
    UserStatusResponseDto updateByUserId(UUID userId);
    void delete(UUID id);


}
