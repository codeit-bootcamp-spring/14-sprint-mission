package com.sprint.mission.discodeit.readstatus.web.dto.req;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequestDTO(
    @NotNull
    UUID userId,

    @NotNull
    UUID channelId,

    @NotNull
    Instant lastReadAt          // ?
) {

}
