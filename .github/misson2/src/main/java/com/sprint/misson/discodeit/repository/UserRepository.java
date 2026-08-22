package com.sprint.misson.discodeit.repository;

import com.sprint.misson.discodeit.entity.User;

public interface UserRepository {

    User create(String name, String email);

    User read(String email);

    void update(User user, String updateName);

    void delete(String email);

    void readAll();
}
