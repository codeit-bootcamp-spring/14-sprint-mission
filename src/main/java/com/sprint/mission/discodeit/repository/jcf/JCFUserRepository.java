package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private static final JCFUserRepository INSTANCE = new JCFUserRepository();  // 싱글톤 패턴 적용

    private static final Map<UUID, User> userMap = new HashMap<>();

    private JCFUserRepository() {}

    public static JCFUserRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public User save(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User find(UUID id) {
        return Optional.ofNullable(userMap.get(id)).orElseThrow();
    }

    @Override
    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        if (userMap.remove(id) == null) {
            throw new NoSuchElementException();
        }
    }
}
