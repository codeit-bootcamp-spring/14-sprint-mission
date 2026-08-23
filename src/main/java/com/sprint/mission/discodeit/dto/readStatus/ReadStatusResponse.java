package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(@NotNull UUID id,
                                 @NotNull Instant createdAt,
                                 @NotNull Instant updatedAt,
                                 @NotNull UUID userId,
                                 @NotNull UUID channelId,
                                 Instant lastReadAt) {
    public static ReadStatusResponse of(ReadStatus created) {
        return new ReadStatusResponse(
                created.getId(),
                created.getCreatedAt(),
                created.getUpdatedAt(),
                created.getUserId(),
                created.getChannelId(),
                created.getLastReadAt()
        );
    }
}
