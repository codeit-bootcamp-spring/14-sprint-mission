package com.sprint.mission.discodeit.application.user.dto;

import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

}
