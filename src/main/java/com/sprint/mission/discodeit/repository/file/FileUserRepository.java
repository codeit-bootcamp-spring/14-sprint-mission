package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.FileStore;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileUserRepository implements UserRepository {

    private static final Path DEFAULT_PATH = Path.of("data", "repository", "users.ser");

    private final FileStore<User> store;

    // 생성자가 여럿이면 Spring이 어느 것을 쓸지 눈에 안 보임.
    public FileUserRepository() {
        this.store = new FileStore<>(DEFAULT_PATH);
    }

    @Override
    public User save(User user) {
        Map<UUID, User> data = store.load();
        data.put(user.getId(), user);
        store.save(data);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(store.load().get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.load().values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, User> data = store.load();
        data.remove(id);
        store.save(data);
    }

    @Override
    public boolean existsByUsername(String username) {
        return store.load().values().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return store.load().values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public void clear() {
        store.clear();
    }
}
