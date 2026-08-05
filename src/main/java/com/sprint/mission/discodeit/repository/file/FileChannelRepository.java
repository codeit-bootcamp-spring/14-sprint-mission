package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileChannelRepository implements ChannelRepository {

    protected Path filePath = Path.of("user.ser");

    @Override
    public void save(Channel channel) {
        Map<UUID, Channel> data = loadData();
        data.put(channel.getId(),channel);
        saveData(data);
    }

    @Override
    public Channel findById(UUID id) {
        return loadData().get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(loadData().values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, Channel> data = loadData();
        data.remove(id);
        saveData(data);
    }

    public void saveData(Map<UUID,Channel> data){
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))){
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Map<UUID, Channel> loadData(){
        if(!Files.exists(filePath))
            return new HashMap<>();
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath.toFile()))){
            return (Map<UUID, Channel>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
