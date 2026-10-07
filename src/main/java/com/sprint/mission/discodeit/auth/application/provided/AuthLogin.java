package com.sprint.mission.discodeit.auth.application.provided;

import com.sprint.mission.discodeit.auth.application.dto.LoginRequest;
import com.sprint.mission.discodeit.user.application.dto.UserDto;

public interface AuthLogin {

  UserDto login(LoginRequest request);
}
