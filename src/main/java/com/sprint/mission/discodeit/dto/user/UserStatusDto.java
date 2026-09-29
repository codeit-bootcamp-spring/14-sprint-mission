package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.domain.user.User;

import java.time.Instant;

public record UserStatusDto(User user,
                            Instant lastActiveAt,
                            Boolean online) {
    public static UserStatusDto from(User user) {
        return new UserStatusDto(
                user,
                user.getLastSeenAt(),
                user.isOnline()
        );
    }
}
