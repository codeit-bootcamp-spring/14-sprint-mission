package com.sprint.mission.discodeit.user.web.dto.res;

import com.sprint.mission.discodeit.user.domain.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDTO(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

}
