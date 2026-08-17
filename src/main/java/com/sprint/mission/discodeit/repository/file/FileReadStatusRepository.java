package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.storage.FileStore;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private static final String FILE_NAME = "read-statuses.ser";

    private final FileStore<ReadStatus> store;

    public FileReadStatusRepository(@Value("${discodeit.repository.file-directory:data/repository}") String fileDirectory) {
        this.store = new FileStore<>(Path.of(fileDirectory, FILE_NAME));
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
