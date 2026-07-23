package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto create(UserCreateRequestDto requestDto);
    UserResponseDto read(UUID id);
    List<UserResponseDto> readAll();
    UserResponseDto update(UserUpdateRequestDto requestDto);
    void delete(UUID id);
}
