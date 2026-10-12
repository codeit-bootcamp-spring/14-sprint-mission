package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto create(UserCreateRequest request, BinaryContentCreateRequest profileRequest);

    UserDto find(UUID userId);

    List<UserDto> findAll();

    UserDto update(UUID userId, UserUpdateRequest request,
        BinaryContentCreateRequest profileRequest);

    void delete(UUID userId);
}
