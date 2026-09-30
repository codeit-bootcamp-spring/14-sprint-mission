package com.sprint.mission.discodeit.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepositoryCustom {
    Optional<Instant> findLatestMessageByChannelId(UUID channelId);
}
