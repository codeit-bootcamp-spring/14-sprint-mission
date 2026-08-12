package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.LoginRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;

public interface AuthService {

    UserResponseDto login(LoginRequestDto loginRequestDto);

}
