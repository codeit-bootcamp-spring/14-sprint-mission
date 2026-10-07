package com.sprint.mission.discodeit.readstatus.application.dto;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

}
