package com.sprint.mission.discodeit.global.security;

import com.sprint.mission.discodeit.user.domain.entity.Role;
import java.util.UUID;

public record JwtPrincipal(UUID userId, Role role) {
}
