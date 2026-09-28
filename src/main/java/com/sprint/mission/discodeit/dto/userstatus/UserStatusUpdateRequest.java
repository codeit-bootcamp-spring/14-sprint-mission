package com.sprint.mission.discodeit.dto.userstatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotNull(message = "newLastActiveAt을 비워둘 수 없습니다.")
    Instant newLastActiveAt
) {
}
