package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.user.UserCreationDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createAccount(UserCreationDto dto);

    UserResponseDto getUser(UUID id);

    List<UserResponseDto> getAllUsers();

    void updateUser(UUID id, UserUpdateDto dto);

    void deleteAccount(UUID id);
}
