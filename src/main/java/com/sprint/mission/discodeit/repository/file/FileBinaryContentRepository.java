package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
public class FileBinaryContentRepository implements BinaryContentRepository {

    protected Path filePath = Path.of("binaryContent.ser");

    @Override
    public void save(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> data = loadData();
        data.put(binaryContent.getId(), binaryContent);
        saveData(data);
    }

    @Override
    public BinaryContent findById(UUID id) {
        return loadData().get(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return loadData().values().stream()
            .filter(binaryContent ->  ids.contains(binaryContent.getId()))
            .toList();
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, BinaryContent> data = loadData();
        data.remove(id);
        saveData(data);
    }
    public void saveData(Map<UUID, BinaryContent> data){
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))){
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Map<UUID, BinaryContent> loadData(){
        if(!Files.exists(filePath))
            return new HashMap<>();
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath.toFile()))){
            return (Map<UUID, BinaryContent>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
