package com.sprint.mission.discodeit.message.application.provided.command;

import java.util.UUID;

public record ModifyMessageCommand(
    UUID messageId,
    String newContent
) {

  public static ModifyMessageCommand to(UUID messageId, String newContent) {
    return new ModifyMessageCommand(messageId, newContent);
  }
}
