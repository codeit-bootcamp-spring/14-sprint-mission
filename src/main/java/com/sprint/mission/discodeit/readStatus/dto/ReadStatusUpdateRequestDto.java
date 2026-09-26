package com.sprint.mission.discodeit.readStatus.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateRequestDto(
         Instant newLastReadAt
) {
}
