package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService extends BasicService<UserResponseDto>{
    UserResponseDto create(UserRequestDto userRequestDto);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    UserResponseDto find(UUID id);
    List<UserResponseDto> findAll();


    void update(UserUpdateRequestDto userUpdateRequestDto);
}
