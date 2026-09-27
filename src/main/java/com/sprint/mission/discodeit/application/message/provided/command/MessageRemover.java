package com.sprint.mission.discodeit.application.message.provided.command;

import java.util.UUID;

public interface MessageRemover {

  void delete(UUID messageId);
}
