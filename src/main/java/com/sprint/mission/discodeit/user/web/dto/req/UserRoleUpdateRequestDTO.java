package com.sprint.mission.discodeit.user.web.dto.req;

import com.sprint.mission.discodeit.user.domain.entity.Role;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserRoleUpdateRequestDTO(
    @NotNull UUID userId,
    @NotNull Role newRole
) {
}
