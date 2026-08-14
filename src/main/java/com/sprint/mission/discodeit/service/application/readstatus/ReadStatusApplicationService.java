package com.sprint.mission.discodeit.service.application.readstatus;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusApplicationService {
    List<ReadStatusResponseDto> findAllByUserId(UUID userId);
    ReadStatusResponseDto markAsRead(UUID userId, UUID channelId);
}
