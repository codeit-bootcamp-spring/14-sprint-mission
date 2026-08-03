package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private static final Map<UUID, User> data = new HashMap<>();

    @Override
    public User create(User user) {
        UUID id = user.getId();

        return findById(id).orElseGet(() -> {
            data.put(id, user);
            return user;
        });
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public boolean existsByNameOrEmail(String name, String email) {
        // TODO 구현해야 함
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateName(UUID id, String name) {
        findById(id).ifPresent(retrieved -> retrieved.updateName(name));
    }

    @Override
    public void deleteById(UUID id) {
        findById(id).ifPresent(retrieved -> data.remove(id));
    }
}
