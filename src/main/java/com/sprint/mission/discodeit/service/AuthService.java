package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.user.UserLoginRequestDto;
import com.sprint.mission.discodeit.entity.dto.user.UserResponseDto;

public interface AuthService {
    UserResponseDto login(UserLoginRequestDto dto);
}
