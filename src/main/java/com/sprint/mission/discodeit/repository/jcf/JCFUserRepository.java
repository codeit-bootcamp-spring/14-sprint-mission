package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public User save(User user) {
        this.data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public List<User> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.data.remove(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.data.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.data.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return this.data.values().stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
    }
}
