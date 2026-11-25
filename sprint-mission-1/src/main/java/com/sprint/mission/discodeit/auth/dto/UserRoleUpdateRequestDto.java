package com.sprint.mission.discodeit.auth.dto;

import com.sprint.mission.discodeit.user.entity.Role;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserRoleUpdateRequestDto(@NotNull UUID userId, @NotNull Role newRole) {

}
