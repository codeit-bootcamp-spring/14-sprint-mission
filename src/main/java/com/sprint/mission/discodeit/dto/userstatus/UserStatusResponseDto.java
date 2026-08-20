package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDto(
    UUID userId,
    Instant lastActiveAt
) {

    public static UserStatusResponseDto from(UserStatus userStatus) {
        return new UserStatusResponseDto(
            userStatus.getUserId(),
            userStatus.getLastActiveAt()
        );
    }
}
