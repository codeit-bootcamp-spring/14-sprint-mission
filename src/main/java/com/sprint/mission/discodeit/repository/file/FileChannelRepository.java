package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static final FileChannelRepository INSTANCE = new FileChannelRepository();

    private final String CHANNEL_FILENAME = "channels.ser";
    private Map<UUID, Channel> channelMap;

    private FileChannelRepository() {}

    public static FileChannelRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Channel save(Channel channel) {
        channelMap = loadFile(CHANNEL_FILENAME);
        channelMap.put(channel.getId(), channel);
        saveToFile(channelMap, CHANNEL_FILENAME);

        return channel;
    }

    @Override
    public Channel find(UUID id) {
        channelMap = loadFile(CHANNEL_FILENAME);
        return Optional.ofNullable(channelMap.get(id)).orElseThrow();
    }

    @Override
    public List<Channel> findAll() {
        channelMap = loadFile(CHANNEL_FILENAME);
        return channelMap.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        channelMap = loadFile(CHANNEL_FILENAME);
        if (channelMap.remove(id) == null) {
            throw new NoSuchElementException();
        }
        saveToFile(channelMap, CHANNEL_FILENAME);
    }



    /**
     * Helper
     */

    private Map<UUID, Channel> loadFile(String filename) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
            return (Map<UUID, Channel>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToFile(Map<UUID, Channel> channelMap, String filename) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            objectOutputStream.writeObject(channelMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
