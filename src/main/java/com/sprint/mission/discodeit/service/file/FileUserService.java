package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserResponse;
import com.sprint.mission.discodeit.dto.userDto.UserRequest;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {

    private final UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {
        return null;
    }

    @Override
    public UserResponse findById(UUID uuid) {
        return null;
    }

    @Override
    public UserResponse update(UUID id,UserRequest userRequest) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public List<UserResponse> readAll() {
        return List.of();
    }
}