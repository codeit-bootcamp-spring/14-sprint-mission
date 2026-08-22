package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {

    private final Map<String, Channel> channels = new HashMap<>();

    public static ObjectOutputStream getOutputStream() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("channel.ser"))) {
            return oos;
        } catch (Exception e) {
            throw new RuntimeException("직렬화 실패");
        }
    }


    @Override
    public Channel save(Channel channel) {
        return null;
    }

    @Override
    public Optional<Channel> findChannel(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public List<Channel> findAll() {
        return List.of();
    }
}