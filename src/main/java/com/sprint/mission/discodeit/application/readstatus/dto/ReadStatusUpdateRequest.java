package com.sprint.mission.discodeit.application.readstatus.dto;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

}
