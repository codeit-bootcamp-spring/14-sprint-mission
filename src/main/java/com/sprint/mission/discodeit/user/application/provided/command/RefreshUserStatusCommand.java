package com.sprint.mission.discodeit.user.application.provided.command;

import java.time.Instant;
import java.util.UUID;

public record RefreshUserStatusCommand(
    UUID userId,
    Instant lastActiveAt
) {

  public static RefreshUserStatusCommand to(UUID userId,
      Instant lastActiveAt
  ) {
    return new RefreshUserStatusCommand(userId, lastActiveAt);
  }
}
