package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.storage.FileStore;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileMessageRepository implements MessageRepository {

    private static final Path DEFAULT_PATH = Path.of("data", "repository", "messages.ser");

    private final FileStore<Message> store;

    // 생성자가 여럿이면 Spring이 어느 것을 쓸지 눈에 안 보임.
    public FileMessageRepository() {
        this.store = new FileStore<>(DEFAULT_PATH);
    }

    @Override
    public Message save(Message message) {
        Map<UUID, Message> data = store.load();
        data.put(message.getId(), message);
        store.save(data);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(store.load().get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(store.load().values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, Message> data = store.load();
        data.remove(id);
        store.save(data);
    }

    public void clear() {
        store.clear();
    }
}
