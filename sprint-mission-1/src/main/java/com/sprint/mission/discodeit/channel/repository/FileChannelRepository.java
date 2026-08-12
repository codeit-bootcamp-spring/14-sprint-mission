package com.sprint.mission.discodeit.channel.repository;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channels = new HashMap<>();
    private final String directory;

    public FileChannelRepository(
        @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        Path path = Paths.get(fileDirectory);
        this.directory = fileDirectory;
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new UncheckedIOException("디렉토리 생성 실패 - path: " + path, e);
        }
        channelLoad();
    }

    private String filePath() {
        return Paths.get(directory, "Channel.ser").toString();
    }

    private void channelLoad() {
        Path file = Paths.get(filePath());
        if (!Files.exists(file)) {
            return;
        }
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
            new FileInputStream(filePath()))) {
            channels.putAll((Map<UUID, Channel>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("기존 채널 데이터가 없습니다. - path: " + filePath(), e);
        }
    }

    private void channelFlush() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
            new FileOutputStream(filePath()))) {
            objectOutputStream.writeObject(this.channels);
        } catch (IOException e) {
            throw new UncheckedIOException("채널 저장에 실패했습니다. - path: " + filePath(), e);
        }
    }

    @Override
    public Channel channelAdd(Channel channel) {
        this.channels.put(channel.getId(), channel);
        channelFlush();
        return channel;
    }

    @Override
    public Optional<Channel> findByChannel(UUID channelId) {
        return Optional.ofNullable(channels.get(channelId));
    }

    @Override
    public void delete(Channel channel) {
        channels.remove(channel.getId());
        channelFlush();
    }

    @Override
    public void update(Channel channel) {
        channels.replace(channel.getId(), channel);
        channelFlush();
    }

    @Override
    public List<Channel> findAllChannel() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public List<Channel> findAllByType(ChannelType channelType) {
        return channels.values().stream()
            .filter(channel -> channel.getChannelType().equals(channelType))
            .toList();
    }
}
