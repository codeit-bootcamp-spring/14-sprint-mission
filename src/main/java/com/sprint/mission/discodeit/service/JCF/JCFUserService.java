package com.sprint.mission.discodeit.service.JCF;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final List<User> users = new ArrayList<>();
    @Override
    public User create(User user) {
        users.add(user);
        return user;
    }

    @Override
    public User find(UUID id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User update(User user) {
        User found = find(user.getId());

        if (found != null){
            found.update(user.getName(), user.getAge());
        }
        return found;
    }

    @Override
    public void delete(UUID id) {

        User found = find(id);

        if (found !=null){
            users.remove(found);
        }
    }
}