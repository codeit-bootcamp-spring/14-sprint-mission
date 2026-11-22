package com.sprint.mission.discodeit.adapter.in.web.controller.auth;

import com.sprint.mission.discodeit.core.domain.user.Role;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserRoleUpdateRequest(@NotNull UUID userId,
                                    @NotNull Role newRole) {

}
