package com.sprint.mission.discodeit.user.application.provided.command;

import com.sprint.mission.discodeit.user.application.dto.UserDto;

public interface UserModifier {

  UserDto modify(ModifyUserCommand command);
}
