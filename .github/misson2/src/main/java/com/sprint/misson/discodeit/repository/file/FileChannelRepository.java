package com.sprint.misson.discodeit.repository.file;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.ChannelType;
import com.sprint.misson.discodeit.repository.ChannelRepository;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

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
    public Channel create(ChannelType channelType, String channelName) {
        return null;
    }

    @Override
    public Channel read(String channelName) {
        return null;
    }

    @Override
    public void update(Channel channel, String updateName) {

    }

    @Override
    public void delete(String channelName) {

    }

    @Override
    public void readAll() {

    }
}