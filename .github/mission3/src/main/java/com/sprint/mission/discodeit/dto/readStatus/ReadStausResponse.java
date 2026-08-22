package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.UUID;

public record ReadStausResponse (
    UUID uuid,
    UUID UserId,
    UUID ChannelId,
    Long createdAt,
    Long recentReadAt

){
    public static ReadStausResponse from(ReadStatus readStatus) {
        return new ReadStausResponse(
            readStatus.getUuid(),
            readStatus.getUserId(),
            readStatus.getChannelId(),
            readStatus.getCreatedAt(),
            readStatus.getRecentReadAt()
        );
    }
}
