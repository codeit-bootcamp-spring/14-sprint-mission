package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
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
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class FileMessageRepository implements MessageRepository {

    protected Path filePath = Path.of("message.ser");

    @Override
    public void save(Message message) {
        Map<UUID, Message> data = loadData();
        data.put(message.getId(),message);
        saveData(data);
    }

    @Override
    public Message findById(UUID id) {
        return loadData().get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(loadData().values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, Message> data = loadData();
        data.remove(id);
        saveData(data);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return loadData().values().stream()
            .filter(message -> message.getChannel_id().equals(channelId))
            .toList();
    }

    public void saveData(Map<UUID,Message> data){
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))){
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Map<UUID, Message> loadData(){
        if(!Files.exists(filePath))
            return new HashMap<>();
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath.toFile()))){
            return (Map<UUID, Message>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
