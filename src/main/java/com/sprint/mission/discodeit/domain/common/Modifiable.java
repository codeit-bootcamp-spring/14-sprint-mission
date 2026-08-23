package com.sprint.mission.discodeit.domain.common;

import java.time.Instant;

public interface Modifiable {
    void markedAsUpdate(Instant newUpdatedAt);
}
