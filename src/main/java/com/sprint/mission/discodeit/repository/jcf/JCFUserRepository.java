package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private Map<UUID, User> userMap;

    public JCFUserRepository() {
        this.userMap = new HashMap<>();
    }

    @Override
    public User save(User user) {
        this.userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User find(UUID id) {
        return Optional.ofNullable(this.userMap.get(id)).orElseThrow();
    }

    @Override
    public List<User> findAll() {
        return this.userMap.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        if (this.userMap.remove(id) == null) {
            throw new NoSuchElementException();
        }
    }
}
