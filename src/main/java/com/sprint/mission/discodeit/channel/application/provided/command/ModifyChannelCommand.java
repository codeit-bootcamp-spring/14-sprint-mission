package com.sprint.mission.discodeit.channel.application.provided.command;

import java.util.UUID;

public record ModifyChannelCommand(
    UUID channelId,
    String newName,
    String newDescription
) {

  public static ModifyChannelCommand to(UUID channelId, String newName, String newDescription) {
    return new ModifyChannelCommand(channelId, newName, newDescription);
  }
}
