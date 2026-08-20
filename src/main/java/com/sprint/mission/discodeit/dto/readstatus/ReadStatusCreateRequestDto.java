package com.sprint.mission.discodeit.dto.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequestDto(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

    public ReadStatus toEntity() {
        return new ReadStatus(
            this.userId,
            this.channelId,
            this.lastReadAt
        );
    }
}
