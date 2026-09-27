package com.sprint.mission.discodeit.application.message.provided.query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface MessageTimeFinder {

  Optional<Instant> getLastMessageAt(UUID channelId);
}
