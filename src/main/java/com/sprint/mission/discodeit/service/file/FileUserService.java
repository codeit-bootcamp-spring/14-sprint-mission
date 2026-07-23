package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    FileUserRepository userRepository;

    public FileUserService() {
        this.userRepository = new FileUserRepository();
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
