package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;
import com.sprint.mission.discodeit.entity.User;

public record UserDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId,
    Boolean online
) {
  public static UserDto toDto(User user, Boolean online) {
    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile() != null ? user.getProfile().getId() : null,
        online
    );
  }
}
