package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.FileStore;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

    private static final Path DEFAULT_PATH = Path.of("data", "repository", "binary-contents.ser");

    private final FileStore<BinaryContent> store;

    public FileBinaryContentRepository() {
        this.store = new FileStore<>(DEFAULT_PATH);
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> data = store.load();
        data.put(binaryContent.getId(), binaryContent);
        store.save(data);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(store.load().get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        Map<UUID, BinaryContent> data = store.load();
        return ids.stream()
                .map(data::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, BinaryContent> data = store.load();
        data.remove(id);
        store.save(data);
    }

    public void clear() {
        store.clear();
    }
}
