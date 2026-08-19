package com.sprint.mission.discodeit.readstatus.dto;

import java.time.Instant;

public record ReadStatusUpdateRequestDto(
    Instant newLastReadAt) {

}
