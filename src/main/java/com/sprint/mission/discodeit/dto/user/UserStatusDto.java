package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.UserStatus;

import java.time.Instant;

public record UserStatusDto(User user,
                            Instant lastActiveAt,
                            Boolean online) {
    public static UserStatusDto of(UserStatus userStatus) {
        return new UserStatusDto(
                userStatus.getUser(),
                userStatus.getLastSeenAt(),
                userStatus.isOnline()
        );
    }
}
