package com.sprint.mission.discodeit.readStatus.dto;

import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequestDto(
         UUID userId,
         UUID channelId,
         Instant lastReadAt
) {
    public ReadStatus toEntity() {
        return new ReadStatus(userId, channelId);
    }
}
