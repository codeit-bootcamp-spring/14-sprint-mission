package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    JCFUserRepository userRepository;

    public JCFUserService() {
        this.userRepository = new JCFUserRepository();
    }

    @Override
    public UserResponseDto create(UserCreateRequestDto requestDto) {
        User user = User.from(requestDto);  // dto를 활용해서 user 객체 만들기 - 이게 맞게 하는건지???   QQQ
        userRepository.save(user);
        return UserResponseDto.from(user);
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
        User user = userRepository.find(requestDto.getId());
        user.update(requestDto.getUsername(), requestDto.getEmail(), requestDto.getPassword());
        return UserResponseDto.from(user);
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
    }
}
