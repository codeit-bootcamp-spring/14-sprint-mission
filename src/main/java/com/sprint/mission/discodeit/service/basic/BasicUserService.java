package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        User savedUser = userRepository.save(user);
        log.info("사용자 생성 완료 : id={}", savedUser.getId());

        return savedUser;
    }

    @Override
    public User findById(UUID id) {
        User user = Optional.ofNullable(userRepository.findById(id))
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
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
    public User update(UUID id, String username, String email, String password) {
        User targetUser = userRepository.findById(id);

        targetUser.update(username, email, password);

        User updatedUser = userRepository.save(targetUser);
        log.info("사용자 수정 완료 : id={}", updatedUser.getId());

        return updatedUser;
    }

    @Override
    public void delete(UUID id) {
        Optional.ofNullable(userRepository.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("삭제할 사용자가 없습니다."));
        userRepository.delete(id);
        log.info("사용자 삭제 완료 : id={}", id);
    }
}
