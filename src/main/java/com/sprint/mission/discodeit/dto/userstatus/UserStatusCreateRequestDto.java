package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequestDto(
    UUID userId,
    Instant lastActiveAt
) {

    public UserStatus toEntity(User user) {
        return new UserStatus(user, this.lastActiveAt);
    }
}
