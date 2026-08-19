package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto userCreate(UserCreateRequestDto userCreateRequestDto);

    UserDto userUpdate(UUID userId, UserUpdateRequestDto userUpdateRequestDto);

    void userDelete(UUID userId);

    List<UserDto> findAll();

    UserDto findById(UUID userId);
}
