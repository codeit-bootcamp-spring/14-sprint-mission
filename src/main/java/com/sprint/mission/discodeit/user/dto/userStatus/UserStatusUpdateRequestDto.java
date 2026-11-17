package com.sprint.mission.discodeit.user.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequestDto(
        Instant newLastActiveAt
) {
}
