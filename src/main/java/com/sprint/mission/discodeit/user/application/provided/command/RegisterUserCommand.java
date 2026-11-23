package com.sprint.mission.discodeit.user.application.provided.command;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;

public record RegisterUserCommand(
    String username,
    String email,
    String password,
    BinaryContentCreateRequest profile
) {

  public static RegisterUserCommand to(String username, String email, String password,
      BinaryContentCreateRequest profile) {
    return new RegisterUserCommand(username, email, password, profile);
  }
}
