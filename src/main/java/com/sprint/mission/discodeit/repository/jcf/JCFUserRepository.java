package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.UserDto;
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
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, UserDto dto) {
        findById(id).ifPresent(retrieved -> retrieved.update(dto));
    }

    @Override
    public void deleteById(UUID id) {
        findById(id).ifPresent(retrieved -> data.remove(id));
    }
}
