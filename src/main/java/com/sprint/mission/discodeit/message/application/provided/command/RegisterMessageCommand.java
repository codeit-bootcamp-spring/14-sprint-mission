package com.sprint.mission.discodeit.message.application.provided.command;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import java.util.List;
import java.util.UUID;

public record RegisterMessageCommand(
    UUID authorId,
    UUID channelId,
    String content,
    List<BinaryContentCreateRequest> attachments
) {

  public static RegisterMessageCommand to(
      UUID authorId,
      UUID channelId,
      String content,
      List<BinaryContentCreateRequest> attachments
  ) {
    return new RegisterMessageCommand(authorId, channelId, content, attachments);
  }
}
