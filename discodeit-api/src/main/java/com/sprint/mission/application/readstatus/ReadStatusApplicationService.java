package com.sprint.mission.application.readstatus;

import com.sprint.mission.controller.dto.readstatus.ReadStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusApplicationService {
    List<ReadStatusResponseDto> findAllByUserId(UUID userId);
    ReadStatusResponseDto markAsRead(UUID userId, UUID channelId);
}
