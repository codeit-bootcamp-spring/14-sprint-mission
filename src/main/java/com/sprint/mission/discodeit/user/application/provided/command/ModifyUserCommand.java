package com.sprint.mission.discodeit.user.application.provided.command;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import java.util.UUID;

public record ModifyUserCommand(
    UUID userId,
    String newUsername,
    String newEmail,
    String newPassword,
    BinaryContentCreateRequest profile
) {

  public static ModifyUserCommand to(
      UUID userId,
      String newUsername,
      String newEmail,
      String newPassword,
      BinaryContentCreateRequest profile
  ) {
    return new ModifyUserCommand(userId, newUsername, newEmail, newPassword, profile);
  }
}
