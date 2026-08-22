package com.sprint.misson.discodeit.service;

import com.sprint.misson.discodeit.entity.User;

public interface UserService {

    User create(String name, String email);

    User read(String email);

    void update(User user, String name);

    void delete(String email);

    void readAll();
}
