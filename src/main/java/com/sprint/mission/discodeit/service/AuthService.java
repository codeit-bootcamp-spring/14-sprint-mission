package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.authdto.AuthCreateRequestDto;
import com.sprint.mission.discodeit.dto.userdto.UserResponseDto;

import java.util.UUID;

public interface AuthService {
    UserResponseDto login(AuthCreateRequestDto authCreateRequestDto);
}
