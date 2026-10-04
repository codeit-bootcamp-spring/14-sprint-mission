package com.sprint.mission.discodeit.adapter.in.controller.dto.user;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserStatusUpdateRequest(@NotNull Instant newLastActiveAt) {
}
