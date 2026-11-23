package com.sprint.mission.discodeit.user.adapter.api.request;

import com.sprint.mission.discodeit.user.domain.Role;
import java.util.UUID;

public record UserRoleUpdateRequest(
    UUID userId,
    Role role
) {

}
