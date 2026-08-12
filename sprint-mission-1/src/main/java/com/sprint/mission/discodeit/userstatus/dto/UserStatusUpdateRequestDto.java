package com.sprint.mission.discodeit.userstatus.dto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequestDto(
    UUID userId,
    Instant lastActiveAt) {

}
