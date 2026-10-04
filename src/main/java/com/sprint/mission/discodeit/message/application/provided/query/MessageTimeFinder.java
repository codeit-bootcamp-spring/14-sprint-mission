package com.sprint.mission.discodeit.message.application.provided.query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface MessageTimeFinder {

  Optional<Instant> getLastMessageAt(UUID channelId);
}
