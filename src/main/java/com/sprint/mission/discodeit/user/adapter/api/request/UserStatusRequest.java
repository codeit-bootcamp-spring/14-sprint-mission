package com.sprint.mission.discodeit.user.adapter.api.request;

import java.time.Instant;
import java.util.UUID;

public record UserStatusRequest(
    UUID uuid,
    UUID userUuid,
    boolean isOnline,
    Instant createdAt,
    Instant activityAt
) {

}
