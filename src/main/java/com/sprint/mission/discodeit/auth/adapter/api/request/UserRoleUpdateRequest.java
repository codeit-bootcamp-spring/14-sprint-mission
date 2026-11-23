package com.sprint.mission.discodeit.auth.adapter.api.request;

import com.sprint.mission.discodeit.user.domain.Role;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserRoleUpdateRequest(
    @NotNull
    UUID userId,
    @NotNull
    Role role
) {

}
