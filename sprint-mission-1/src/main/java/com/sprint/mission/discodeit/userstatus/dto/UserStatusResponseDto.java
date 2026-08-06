package com.sprint.mission.discodeit.userstatus.dto;

import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDto(UUID id, UUID userId, boolean online, Instant lastActiveAt) {

    public static UserStatusResponseDto from(UserStatus userStatus) {
        return new UserStatusResponseDto(
            userStatus.getUserStatusId(),
            userStatus.getUserId(),
            userStatus.isOnline(),
            userStatus.getLastActiveAt()
        );
    }
}
