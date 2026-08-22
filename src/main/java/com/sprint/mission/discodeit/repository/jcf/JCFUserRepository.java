package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data;

    @Override
    public User save(User user) {
        System.out.println(data);
        UUID uuid = user.getId();
        data.put(uuid, user);
        System.out.println("data = " + data);
        System.out.println("data.getClass() = " + data.getClass());
        return data.get(uuid);
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return Optional.ofNullable(data.get(uuid));
    }

    @Override
    public void delete(UUID uuid) {
        data.remove(uuid);
    }

    @Override
    public List<User> readAll() {
        return data.values().stream().toList();
    }
}