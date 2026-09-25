package com.sprint.mission.discodeit.adaptor.dto.userStatusDto;

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
