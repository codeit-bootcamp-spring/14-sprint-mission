package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDto userCreate(UserCreateRequestDto userCreateRequestDto);

    UserResponseDto userUpdate(UUID userId, UserUpdateRequestDto userUpdateRequestDto);

    void userDelete(UUID userId);

    List<UserResponseDto> findAll();

    UserResponseDto findById(UUID userId);
}
