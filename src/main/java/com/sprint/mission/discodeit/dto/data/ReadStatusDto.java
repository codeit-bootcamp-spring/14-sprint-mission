package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;
import com.sprint.mission.discodeit.entity.ReadStatus;

public record ReadStatusDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {
  public static ReadStatusDto toDto(ReadStatus readStatus) {
    return new ReadStatusDto(
        readStatus.getId(),
        readStatus.getCreatedAt(),
        readStatus.getUpdatedAt(),
        readStatus.getUser().getId(),
        readStatus.getChannel().getId(),
        readStatus.getLastReadAt()
    );
  }
}
