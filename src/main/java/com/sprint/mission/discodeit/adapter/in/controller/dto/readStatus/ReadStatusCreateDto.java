package com.sprint.mission.discodeit.adapter.in.controller.dto.readStatus;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateDto(@NotNull UUID userId,
                                  @NotNull UUID channelId,
                                  @NotNull Instant lastReadAt) {

}
