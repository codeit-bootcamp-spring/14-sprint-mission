package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserStatusUpdateRequest(@NotNull Instant newLastActiveAt) {
}
