package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userdto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.userdto.UserResponseDto;
import com.sprint.mission.discodeit.dto.userdto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto createUser(UserCreateRequestDto requestDto) {
        User user = requestDto.toEntity();
        userRepository.save(user);
        return UserResponseDto.from(user);
    }

    @Override
    public UserResponseDto readUser(UUID id) {
        User user = userRepository.findById(id);
        return UserResponseDto.from(user);
    }

    @Override
    public List<UserResponseDto> readAllUser() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(UserResponseDto.from(user));
        }
        return responses;
    }

    @Override
    public UserResponseDto updateUser(UUID id, UserUpdateRequestDto requestDto) {
        User target = userRepository.findById(id);
        if (target == null) {
            throw new RuntimeException("해당 유저가 존재하지 않습니다: " + id);
        }
        target.setName(requestDto.getName());
        target.setUpdatedAt();
        userRepository.save(target);
        return UserResponseDto.from(target);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.delete(id);
    }
}
