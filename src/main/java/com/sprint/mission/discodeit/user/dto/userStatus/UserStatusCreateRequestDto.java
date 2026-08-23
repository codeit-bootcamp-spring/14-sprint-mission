package com.sprint.mission.discodeit.user.dto.userStatus;

import com.sprint.mission.discodeit.user.domain.UserStatus;

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
