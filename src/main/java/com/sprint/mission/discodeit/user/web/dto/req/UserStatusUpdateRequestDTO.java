package com.sprint.mission.discodeit.user.web.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;

public record UserStatusUpdateRequestDTO(
    @NotNull
    @PastOrPresent
    Instant newLastActiveAt
) {

}
