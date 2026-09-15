package com.sprint.mission.discodeit.service.IService;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDto;
import java.util.List;
import java.util.UUID;

public interface IUserStatusService {
    UserStatusResponseDto create(UserStatusCreateRequestDto request);
    UserStatusResponseDto find(UUID id);
    List<UserStatusResponseDto> findAll();
    UserStatusResponseDto update(UUID id, UserStatusUpdateDto request);
    UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateDto request);
    void deleteById(UUID id);


}
