package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotNull(message = "newLastReadAt을 비워둘 수 없습니다.")
    Instant newLastReadAt
) {
}
