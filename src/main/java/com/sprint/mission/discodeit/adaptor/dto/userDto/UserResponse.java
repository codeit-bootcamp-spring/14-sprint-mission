package com.sprint.mission.discodeit.adaptor.dto.userDto;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    boolean online
) {

  public static UserResponse from(User user, UserStatus userStatus) {
    return new UserResponse(user.getId(), user.getCreatedAt(), userStatus.getUpdatedAt(),
        user.getName(), user.getEmail(),
        userStatus.isOnline());
  }
}
