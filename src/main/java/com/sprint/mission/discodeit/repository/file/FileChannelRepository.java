package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {

    private final String CHANNEL_FILENAME = "channels.ser";

    // FileChannelRepository 생성 시 한번만 로드
    private Map<UUID, Channel> channelMap = loadFile(CHANNEL_FILENAME);

    private FileChannelRepository() {}

    private static class LazyHolder {
        private static final FileChannelRepository INSTANCE = new FileChannelRepository();
    }

    public static FileChannelRepository getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public Channel save(Channel channel) {
        channelMap.put(channel.getId(), channel);
        saveToFile(channelMap, CHANNEL_FILENAME);
        return channel;
    }

    @Override
    public Channel find(UUID id) {
        return Optional.ofNullable(channelMap.get(id))
                .orElseThrow(() -> new CustomException(ExceptionType.CHANNEL_NOT_FOUND));
    }

    @Override
    public List<Channel> findAll() {
        return channelMap.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        if (channelMap.remove(id) == null) {
            throw new CustomException(ExceptionType.CHANNEL_NOT_FOUND);
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
