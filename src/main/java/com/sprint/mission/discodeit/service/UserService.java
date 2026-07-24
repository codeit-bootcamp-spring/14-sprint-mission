package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.user.UserCreationDto;
import com.sprint.mission.discodeit.entity.dto.user.UserUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User createAccount(UserCreationDto dto);

    Optional<User> getUser(UUID id);

    List<User> getAllUsers();

    void updateUser(UUID id, UserUpdateDto dto);

    void deleteAccount(UUID id);
}
