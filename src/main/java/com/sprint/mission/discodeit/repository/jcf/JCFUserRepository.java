package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
    final List<User> userList;

    public JCFUserRepository() {
        this.userList = new ArrayList<>();
    }

    @Override
    public void save(User user) {
        userList.add(user);
    }

    @Override
    public User findById(UUID id) {
        for (User each : userList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return this.userList;
    }

    @Override
    public void delete(UUID id) {
        User target = findById(id);
        if (target != null) {
            userList.remove(target);
        }
    }
}
