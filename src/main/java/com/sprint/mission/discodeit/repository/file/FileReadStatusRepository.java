package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.storage.FileStore;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private static final Path DEFAULT_PATH = Path.of("data", "repository", "read-statuses.ser");

    private final FileStore<ReadStatus> store;

    public FileReadStatusRepository() {
        this.store = new FileStore<>(DEFAULT_PATH);
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Map<UUID, ReadStatus> data = store.load();
        data.put(readStatus.getId(), readStatus);
        store.save(data);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(store.load().get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return store.load().values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return store.load().values().stream()
                .filter(status -> status.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, ReadStatus> data = store.load();
        data.remove(id);
        store.save(data);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> data = store.load();
        data.values().removeIf(status -> status.getChannelId().equals(channelId));
        store.save(data);
    }

    public void clear() {
        store.clear();
    }
}
