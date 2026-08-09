package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public record ReadStatusCreateRequestDto(
         UUID userId,
         UUID channelId
) {
    public ReadStatus toEntity() {
        return new ReadStatus(userId, channelId);
    }
}
