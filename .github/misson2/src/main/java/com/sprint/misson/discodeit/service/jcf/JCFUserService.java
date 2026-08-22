package com.sprint.misson.discodeit.service.jcf;

import com.sprint.misson.discodeit.entity.User;
import com.sprint.misson.discodeit.repository.UserRepository;
import com.sprint.misson.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.misson.discodeit.service.UserService;

public class JCFUserService implements UserService {

    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String name, String email) {
        return userRepository.create(name, email);
    }

    @Override
    public User read(String name) {
        return userRepository.read(name);
    }

    @Override
    public void update(User user, String updateName) {
        userRepository.update(user, updateName);
    }

    @Override
    public void delete(String email) {
        userRepository.delete(email);
    }

    @Override
    public void readAll() {
        userRepository.readAll();
    }
}