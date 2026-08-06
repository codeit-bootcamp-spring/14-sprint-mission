package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    final List<User> userList;

    public JCFUserService() {
        this.userList = new ArrayList<>();
    }

    @Override

    //Crate
    public User createUser(String email, String name) {
        User user = new User(email, name);
        userList.add(user);
        return user;
    }

    @Override
    //Read (id를 받아 User반환)
    public User readUser(UUID id) {
        for (User each : userList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    //Read (전체 조회)
    public List<User> readAllUser() {
        return this.userList;
    }

    @Override
    //Update
    public void updateUser(UUID id, String name) {
        User user = readUser(id);
        user.setName(name);
        user.setUpdatedAt();
//        return user;
    }

    @Override
    //Delete
    public void deleteUser(UUID id) {
        User user = readUser(id);
        userList.remove(user);
    }
}
