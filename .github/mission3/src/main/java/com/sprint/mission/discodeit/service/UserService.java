package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserResponse;
import com.sprint.mission.discodeit.dto.userDto.UserRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse create(UserCreateRequest userCreateRequest);

    UserResponse findById(UUID uuid);

    UserResponse update(UserRequest userRequest);

    void delete(UUID uuid);

    List<UserResponse> readAll();
}
