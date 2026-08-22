package com.sprint.misson.discodeit.repository.file;

import com.sprint.misson.discodeit.entity.User;
import com.sprint.misson.discodeit.repository.UserRepository;

public class FileUserRepository implements UserRepository {


    @Override
    public User create(String name, String email) {
        return null;
    }

    @Override
    public User read(String email) {
        return null;
    }

    @Override
    public void update(User user, String updateName) {

    }

    @Override
    public void delete(String email) {

    }

    @Override
    public void readAll() {

    }
}
