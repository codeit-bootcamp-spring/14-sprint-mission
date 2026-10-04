package com.sprint.mission.discodeit.adapter.in.controller.dto.message;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageCreateRequest(@NotNull String content,
                                   @NotNull UUID channelId,
                                   @NotNull UUID authorId) {

}
