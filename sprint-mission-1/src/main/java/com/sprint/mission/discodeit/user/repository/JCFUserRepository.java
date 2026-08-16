package com.sprint.mission.discodeit.user.repository;

import com.sprint.mission.discodeit.user.entity.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public User userAdd(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findByUser(UUID userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> findByUserName(String name) {
        return users.values().stream()
            .filter(user -> name.equals(user.getName()))
            .findFirst();
    }

    @Override
    public Optional<User> findByUserEmail(String email) {
        return users.values().stream()
            .filter(user -> email.equals(user.getEmail()))
            .findFirst();
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public void update(User user) {
        users.replace(user.getId(), user);
    }

    @Override
    public List<User> findAllUser() {
        return new ArrayList<>(users.values());
    }
}
