package com.sprint.mission.discodeit.auth.application;

import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.auth.dto.AuthLoginResponseDto;
import com.sprint.mission.discodeit.user.dto.UserDto;

public interface AuthService {

    UserDto login(AuthLoginRequestDto authLoginRequestDto);

}
