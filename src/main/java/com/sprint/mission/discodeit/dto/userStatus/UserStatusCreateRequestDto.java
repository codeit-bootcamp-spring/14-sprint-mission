package com.sprint.mission.discodeit.dto.userStatus;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequestDto(
        UUID userId,
        Instant lastAccessAt
) {
    public UserStatus toEntity() {
            return new UserStatus(userId);

    }
}
