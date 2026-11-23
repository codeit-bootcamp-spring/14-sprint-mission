package com.sprint.mission.discodeit.user.adapter.api.request;

import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

}
