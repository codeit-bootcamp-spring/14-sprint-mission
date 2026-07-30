package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.discodeit.service.basic.BasicChannelService.ERROR_CHANNEL_NOT_FOUND;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private static final String FILE_PATH = "channels.ser";

    private Map<UUID, Channel> loadData(){
        File file = new File(FILE_PATH);
        if (!file.exists()){
            return new HashMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, Channel> data){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Channel create(UUID creatorId, String name) {
        Map<UUID, Channel> data = loadData();
        Channel channel = Channel.create(creatorId, name);
        data.put(channel.getId(), channel);
        saveData(data);
        return channel;
    }

    @Override
    public Optional<Channel> read(UUID id) {
        Map<UUID, Channel> data = loadData();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> readAll() {
        Map<UUID, Channel> data = loadData();
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, String name) {
        Map<UUID, Channel> data = loadData();
        Channel channel = Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND + id));
            channel.changeName(name);
            saveData(data);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> data = loadData();
        data.remove(id);
        saveData(data);
    }
}
