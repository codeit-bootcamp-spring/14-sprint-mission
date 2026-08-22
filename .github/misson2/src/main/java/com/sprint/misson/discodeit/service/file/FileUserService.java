package com.sprint.misson.discodeit.service.file;

import com.sprint.misson.discodeit.entity.User;
import com.sprint.misson.discodeit.repository.UserRepository;
import com.sprint.misson.discodeit.service.UserService;

public class FileUserService implements UserService {

    private final UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String name, String email) {
        return userRepository.create(name, email);
    }

    @Override
    public User read(String email) {
        return userRepository.read(email);
    }

    @Override
    public void update(User user, String name) {
        userRepository.update(user, name);
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