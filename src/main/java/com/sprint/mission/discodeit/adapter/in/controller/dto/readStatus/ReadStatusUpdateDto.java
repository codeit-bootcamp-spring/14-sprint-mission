package com.sprint.mission.discodeit.adapter.in.controller.dto.readStatus;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ReadStatusUpdateDto(@NotNull Instant newLastReadAt) {

}
