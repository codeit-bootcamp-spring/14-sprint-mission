package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.entity.Role;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserRoleUpdateRequest(
    @NotNull(message = "userId를 비워둘 수 없습니다.")
    UUID userId,
    @NotNull(message = "newRole을 비워둘 수 없습니다.")
    Role newRole
) {
}
