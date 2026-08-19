package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.LoginRequestDto;
import com.sprint.mission.discodeit.user.dto.UserDto;

public interface AuthService {

    UserDto login(LoginRequestDto loginRequestDto);

}
