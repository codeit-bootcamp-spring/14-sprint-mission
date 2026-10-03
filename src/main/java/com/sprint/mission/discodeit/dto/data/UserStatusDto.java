package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;
import com.sprint.mission.discodeit.entity.UserStatus;

public record UserStatusDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    Instant lastActiveAt,
    Boolean online
) {
  public static UserStatusDto toDto(UserStatus userStatus) {
    return new UserStatusDto(
        userStatus.getId(),
        userStatus.getCreatedAt(),
        userStatus.getUpdatedAt(),
        userStatus.getUser().getId(),
        userStatus.getLastActiveAt(),
        userStatus.isOnline()
    );
  }
}
