package com.sprint.mission.discodeit.application.user.provided.command;

import com.sprint.mission.discodeit.application.user.dto.UserDto;
import com.sprint.mission.discodeit.application.user.dto.UserUpdateRequest;
import java.util.UUID;

public interface UserModifier {

  UserDto modify(UUID userId, UserUpdateRequest request);
}
