package com.sprint.mission.discodeit.user.application.dto;

import com.sprint.mission.discodeit.user.domain.Role;
import java.util.UUID;

public record UserRoleUpdateRequest(
    UUID userId,
    Role role
) {

}
