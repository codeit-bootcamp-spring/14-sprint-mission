package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.channel.ChannelUpdateNameDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.File;
import java.util.*;

public class FileChannelRepository extends MapFileIO<Channel>
        implements ChannelRepository {

    protected final Map<UUID, Channel> EMPTY_BUFFER = new HashMap<>();
    private Map<UUID, Channel> buffer;

    public FileChannelRepository(String fileName) {
        super(new File(fileName));
        this.buffer = Optional.of(file)
                .filter(file -> file.exists() && file.length() != 0)
                .map(file -> super.readFile(file))
                .orElseGet(() -> super.writeFile(file, EMPTY_BUFFER));
    }

    @Override
    public Channel create(Channel channel) {
        UUID channelId = channel.getId();
        return findById(channelId).orElseGet(() -> {
            buffer.put(channelId, channel);
            writeFile();
            return channel;
        });
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        buffer = super.readFile(file);
        return Optional.ofNullable(buffer.get(id));
    }

    @Override
    public List<Channel> findAll() {
        buffer = super.readFile(file);
        return new ArrayList<>(buffer.values());
    }

    @Override
    public void updateName(UUID id, ChannelUpdateNameDto dto) {
        findById(id).ifPresent(retrieved -> {
            retrieved.updateName(dto);
            writeFile();
        });
    }

    @Override
    public void deleteById(UUID id) {
        findById(id).ifPresent(retrieved -> {
            buffer.remove(id);
            writeFile();
        });
    }

    @Override
    public void deleteUsersByUserId(UUID userId) {
        buffer.values().forEach(channel -> channel.getUsersId().remove(userId));
        writeFile();
    }

    private void writeFile() {
        super.writeFile(file, buffer);
    }
}
