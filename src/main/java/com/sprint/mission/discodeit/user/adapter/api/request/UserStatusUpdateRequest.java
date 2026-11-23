package com.sprint.mission.discodeit.user.adapter.api.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotNull
    Instant newLastActiveAt
) {

}
