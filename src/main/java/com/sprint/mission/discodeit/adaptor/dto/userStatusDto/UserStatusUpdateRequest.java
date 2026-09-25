package com.sprint.mission.discodeit.adaptor.dto.userStatusDto;

import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

}
