package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.storage.FileStore;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileChannelRepository implements ChannelRepository {

    private static final Path DEFAULT_PATH = Path.of("data", "repository", "channels.ser");

    private final FileStore<Channel> store;

    // 생성자가 여럿이면 Spring이 어느 것을 쓸지 눈에 안 보임.
    public FileChannelRepository() {
        this.store = new FileStore<>(DEFAULT_PATH);
    }

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> data = store.load();
        data.put(channel.getId(), channel);
        store.save(data);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(store.load().get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(store.load().values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, Channel> data = store.load();
        data.remove(id);
        store.save(data);
    }

    public void clear() {
        store.clear();
    }
}
