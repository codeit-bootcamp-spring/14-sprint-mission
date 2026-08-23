package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserDto(UUID id,
                      Instant createdAt,
                      Instant updatedAt,
                      String username,
                      String email,
                      UUID profileId,
                      Boolean online) {

    public static UserDto of(User user, UserStatus userStatus) {
        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getEmail(),
                user.getProfileId(),
                userStatus.isOnline()
        );
    }
}
