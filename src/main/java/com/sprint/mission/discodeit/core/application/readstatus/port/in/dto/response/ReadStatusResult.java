package com.sprint.mission.discodeit.core.application.readstatus.port.in.dto.response;

import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResult(UUID id, UUID userId, UUID channelId, Instant lastReadAt) {
    public static ReadStatusResult from(ReadStatus readStatus) {
        return new ReadStatusResult(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt()
        );
    }
}
