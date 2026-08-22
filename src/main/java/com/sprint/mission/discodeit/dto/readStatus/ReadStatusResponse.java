package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.UUID;

public record ReadStatusResponse(
    UUID uuid,
    UUID UserId,
    UUID ChannelId,
    Long createdAt,
    Long recentReadAt

){
    public static ReadStatusResponse from(ReadStatus readStatus) {
        return new ReadStatusResponse(
            readStatus.getUuid(),
            readStatus.getUserId(),
            readStatus.getChannelId(),
            readStatus.getCreatedAt(),
            readStatus.getRecentReadAt()
        );
    }
}
