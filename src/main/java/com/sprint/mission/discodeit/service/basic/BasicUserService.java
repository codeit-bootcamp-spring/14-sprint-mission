package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        User savedUser = userRepository.save(user);
        log.info("사용자 생성 완료 : id={}", savedUser.getId());

        return savedUser;
    }

    @Override
    public User findById(UUID id) {
        User user = userRepository.findById(id);
        log.info("사용자 조회 : id={}", user.getId());

        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        log.info("사용자 전체 조회 : count={}", users.size());

        return users;
    }

    @Override
    public User update(UUID id, User user) {
        User targetUser = userRepository.findById(id);

        targetUser.update(user.getUsername(), user.getEmail(), user.getPassword());

        User updatedUser = userRepository.save(targetUser);
        log.info("사용자 수정 완료 : id={}", updatedUser.getId());

        return updatedUser;
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
        log.info("사용자 삭제 완료 : id={}", id);
    }
}
