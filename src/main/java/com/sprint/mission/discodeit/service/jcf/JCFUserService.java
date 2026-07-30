package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private static JCFUserService instance;
    private final Map<UUID, User> data;

    private JCFUserService() {
        this.data = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }
        return instance;
    }

    @Override
    public User create(String name, String email) {
        User user = new User(name, email);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        return data.get(userId);
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().collect(Collectors.toList());
    }

    @Override
    public User update(UUID userId, String name, String email) {
        User user = data.get(userId);
        if (user != null) {
            user.update(name, email);
        }
        return user;
    }

    @Override
    public void delete(UUID userId) {
        data.remove(userId);
    }
}