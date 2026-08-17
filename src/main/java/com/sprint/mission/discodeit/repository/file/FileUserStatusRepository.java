package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.FileStore;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
@Repository
public class FileUserStatusRepository implements UserStatusRepository {

    private static final String FILE_NAME = "user-statuses.ser";

    private final FileStore<UserStatus> store;

    public FileUserStatusRepository(@Value("${discodeit.repository.file-directory:data/repository}") String fileDirectory) {
        this.store = new FileStore<>(Path.of(fileDirectory, FILE_NAME));
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Map<UUID, UserStatus> data = store.load();
        data.put(userStatus.getId(), userStatus);
        store.save(data);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(store.load().get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return store.load().values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(store.load().values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, UserStatus> data = store.load();
        data.remove(id);
        store.save(data);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Map<UUID, UserStatus> data = store.load();
        data.values().removeIf(status -> status.getUserId().equals(userId));
        store.save(data);
    }

    public void clear() {
        store.clear();
    }
}
