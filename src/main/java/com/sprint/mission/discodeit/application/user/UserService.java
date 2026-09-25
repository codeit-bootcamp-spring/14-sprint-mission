package com.sprint.mission.discodeit.application.user;

import com.sprint.mission.discodeit.adaptor.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.adaptor.dto.userDto.UserResponse;
import com.sprint.mission.discodeit.adaptor.dto.userDto.UserUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {

  UserResponse create(UserCreateRequest userCreateRequest);

  UserResponse findById(UUID uuid);

  UserResponse update(UUID id, UserUpdateRequest userUpdateRequest);

  void delete(UUID uuid);

  List<UserResponse> readAll();
}
