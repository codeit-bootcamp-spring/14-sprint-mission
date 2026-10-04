package com.sprint.mission.discodeit.message.application.provided.command;

import java.util.UUID;

public interface MessageRemover {

  void delete(UUID messageId);
}
