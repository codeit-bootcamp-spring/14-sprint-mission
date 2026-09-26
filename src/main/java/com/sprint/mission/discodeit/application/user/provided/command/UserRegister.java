package com.sprint.mission.discodeit.application.user.provided.command;

import com.sprint.mission.discodeit.application.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.application.user.dto.UserDto;

public interface UserRegister {

  UserDto register(UserCreateRequest request);
}
