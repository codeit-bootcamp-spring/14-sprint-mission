package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequestDto(
    UUID userId,
    Instant lastActiveAt
) {

    public UserStatus toEntity() {
        return new UserStatus(this.userId, this.lastActiveAt);
    }
}
