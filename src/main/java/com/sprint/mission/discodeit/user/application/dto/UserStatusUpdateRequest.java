package com.sprint.mission.discodeit.user.application.dto;

import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

}
