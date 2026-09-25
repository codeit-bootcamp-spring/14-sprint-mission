package com.sprint.mission.discodeit.adaptor.dto.readStatus;

import com.sprint.mission.discodeit.domain.ReadStatus;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
    UUID uuid,
    UUID userId,
    UUID channelId,
    Instant createdAt,
    Instant recentReadAt

) {

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
