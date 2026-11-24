package com.sprint.mission.discodeit.user.application.dto;

import java.util.UUID;

public record UserRoleChanged(
    UUID userId
) {

  public static UserRoleChanged to(UUID userId) {
    return new UserRoleChanged(userId);
  }
}
