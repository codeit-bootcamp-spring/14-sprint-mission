package com.sprint.mission.discodeit.application.readstatus.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
