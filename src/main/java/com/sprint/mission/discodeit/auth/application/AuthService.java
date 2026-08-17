package com.sprint.mission.discodeit.auth.application;

import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.auth.dto.AuthLoginResponseDto;

public interface AuthService {

    AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto);

}
