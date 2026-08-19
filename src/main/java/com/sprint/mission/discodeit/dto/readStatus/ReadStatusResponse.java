package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(UUID readStatusId,
                                 UUID userId,
                                 UUID channelId,
                                 Instant lastReadAt) {
    public static ReadStatusResponse of(ReadStatus readStatus) {
        return new ReadStatusResponse(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt()
        );
    }
}
