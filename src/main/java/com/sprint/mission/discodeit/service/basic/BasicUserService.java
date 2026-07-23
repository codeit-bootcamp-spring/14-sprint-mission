package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto create(UserCreateRequestDto requestDto) {
        User newUser = User.from(requestDto);
        userRepository.save(newUser);
        return UserResponseDto.from(newUser);
    }

    @Override
    public UserResponseDto read(UUID id) {
        return UserResponseDto.from(
                userRepository.find(id)
        );
    }

    @Override
    public List<UserResponseDto> readAll() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::from)
                .toList();
    }

    @Override
    public UserResponseDto update(UserUpdateRequestDto requestDto) {
        User userToUpdate = userRepository.find(requestDto.getId());
        userToUpdate.update(requestDto.getUsername(), requestDto.getEmail(), requestDto.getPassword());
        userRepository.save(userToUpdate);
        return UserResponseDto.from(userToUpdate);
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
    }
}
