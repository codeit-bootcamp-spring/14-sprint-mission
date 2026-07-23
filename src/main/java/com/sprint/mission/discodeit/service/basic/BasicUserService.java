package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.user.UserDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    @Override
    public User createAccount(UserDto dto) {
        User user = new User(dto.getName());
        return userRepository.create(user);
    }

    @Override
    public Optional<User> getUser(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID id, UserDto dto) {
        userRepository.update(id, dto);
    }

    @Override
    public void deleteAccount(UUID id) {
        // 유저 삭제 시,
        // 1. 해당 유저의 message를 삭제해야 한다.
        // 2. 모든 channel에서 해당 유저를 삭제해야 한다.
        channelRepository.deleteUsersByUserId(id);
        messageRepository.deleteAllByUserId(id);
        userRepository.deleteById(id);
    }
}
