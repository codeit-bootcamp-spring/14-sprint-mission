package com.sprint.mission.discodeit.readStatus.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequestDto(
         @NotNull(message = "userId는 필수입니다.")
         UUID userId,

         @NotNull(message = "channelId는 필수입니다.")
         UUID channelId,

         Instant lastReadAt
) {

}
