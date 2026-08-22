package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.Instant;
import java.util.UUID;

public class BinaryContent {
    private UUID uuid;
    private final Long createdAt = Instant.now();
    private Long updatedAt;

}
