package com.sprint.mission.discodeit.readStatus.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ReadStatusUpdateRequestDto(
         @NotNull(message = "newLastReadAt은 필수입니다.")
         Instant newLastReadAt
) {
}
