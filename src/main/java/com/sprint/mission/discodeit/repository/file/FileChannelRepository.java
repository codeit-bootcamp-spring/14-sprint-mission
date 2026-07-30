package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_NAME = "channels.dat";

    private Map<UUID, Channel> loadData() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, Channel> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생하였습니다.");
        }
    }

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> data = loadData();
        data.put(channel.getId(), channel);

        saveData(data);

        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return loadData().get(id);
    }

    @Override
    public List<Channel> findAll() {
        return loadData().values()
                .stream()
                .toList();
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> data = loadData();
        data.remove(id);

        saveData(data);
    }
}
