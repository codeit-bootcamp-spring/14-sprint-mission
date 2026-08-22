package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.LoginRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponse;

public interface AuthService {

    UserResponse login(LoginRequestDto loginRequestDto);

}
