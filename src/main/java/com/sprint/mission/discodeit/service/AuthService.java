package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.authLogin.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.authLogin.AuthLoginResponseDto;

public interface AuthService {

    AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto);

}
