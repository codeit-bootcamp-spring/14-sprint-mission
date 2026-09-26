package com.sprint.mission.discodeit.application.auth;

import com.sprint.mission.discodeit.application.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.application.user.dto.UserDto;

public interface AuthLogin {

  UserDto login(LoginRequest request);
}
