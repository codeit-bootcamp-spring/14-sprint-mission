package com.sprint.mission.discodeit.user.application.provided.command;

import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.domain.Role;
import java.util.UUID;

public interface UserRoleModifier {

  UserDto updateRole(UUID userId, Role role);
}
