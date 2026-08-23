package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class FileReadStatusRepository implements ReadStatusRepository {
    protected Path filePath = Path.of("readStatus.ser");

    @Override
    public void save(ReadStatus readStatus) {
        Map<UUID, ReadStatus> data = loadData();
        data.put(readStatus.getId(), readStatus);
        saveData(data);
    }

    @Override
    public ReadStatus findById(UUID id) {
        return loadData().get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return loadData().values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, ReadStatus> data = loadData();
        data.remove(id);
        saveData(data);

    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return loadData().values().stream()
            .filter(readStatus -> readStatus.getChannelId().equals(channelId))
            .toList();
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return loadData().values().stream()
            .anyMatch(readStatus -> readStatus.getUserId().equals(userId) &&
                readStatus.getChannelId().equals(channelId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return loadData().values().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId))
            .toList();
    }
    public void saveData(Map<UUID, ReadStatus> data){
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))){
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Map<UUID, ReadStatus> loadData(){
        if(!Files.exists(filePath))
            return new HashMap<>();
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath.toFile()))){
            return (Map<UUID, ReadStatus>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
