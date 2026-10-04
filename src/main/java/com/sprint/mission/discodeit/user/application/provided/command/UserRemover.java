package com.sprint.mission.discodeit.user.application.provided.command;

import java.util.UUID;

public interface UserRemover {

  void delete(UUID userId);
}
