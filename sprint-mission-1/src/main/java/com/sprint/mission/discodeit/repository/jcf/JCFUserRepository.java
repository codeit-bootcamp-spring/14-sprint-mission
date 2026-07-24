package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class JCFUserRepository implements UserRepository {
    private final static List<User> users = new ArrayList<>();

    @Override
    public User userAdd(User user) {
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> findByUser(String userName) {
        return users.stream()
                .filter(user -> userName.equals(user.getUserName()))
                .findFirst();
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }

    @Override
    public List<User> findAllUser() {
        return new ArrayList<>(users);
    }
}
