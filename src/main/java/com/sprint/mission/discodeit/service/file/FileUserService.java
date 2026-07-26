package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    FileUserRepository fileUserRepository;

    public FileUserService(FileUserRepository fileUserRepository){
        this.fileUserRepository = fileUserRepository;
    }


    @Override
    public User create(String userName, String email, String password) {
        User user = new User(userName, email, password);
        fileUserRepository.save(user);
        return user;
    }

    @Override
    public User read(UUID id) {
        return null;
    }

    @Override
    public void update(User t) {

    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public List<User> readAll() {
        return List.of();
    }
}
