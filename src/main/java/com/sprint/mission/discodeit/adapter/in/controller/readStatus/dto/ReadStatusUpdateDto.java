package com.sprint.mission.discodeit.adapter.in.controller.readStatus.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ReadStatusUpdateDto(@NotNull Instant newLastReadAt) {

}
