package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull(message = "userId를 비워둘 수 없습니다.")
    UUID userId,
    @NotNull(message = "channelId를 비워둘 수 없습니다.")
    UUID channelId,
    Instant lastReadAt
) {
}
