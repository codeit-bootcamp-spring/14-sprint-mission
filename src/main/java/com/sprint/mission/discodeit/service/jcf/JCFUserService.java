package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        userRepository.save(user);
        System.out.println("사용자 등록이 완료되었습니다.");

        return user;
    }

    @Override
    public User findById(UUID id) {
        User user = Optional.ofNullable(userRepository.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID id, User user) {
        User targetUser = Optional.ofNullable(userRepository.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("수정할 사용자가 없습니다."));

        targetUser.update(user.getName(), user.getAge(), user.getEmail());

        userRepository.save(targetUser);
        System.out.println("사용자 정보 수정이 완료되었습니다.");

        return targetUser;
    }

    @Override
    public void delete(UUID id) {
        User targetUser = Optional.ofNullable(userRepository.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("삭제할 사용자가 없습니다."));

        userRepository.delete(id);
        System.out.println("사용자 정보 삭제를 완료하였습니다.");
    }
}
