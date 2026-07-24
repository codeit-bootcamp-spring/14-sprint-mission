package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.BasicEntity;
import com.sprint.mission.discodeit.service.Service;
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
import java.util.Objects;
import java.util.UUID;

public abstract class FileService<T extends BasicEntity> implements Service<T> {

    protected final Path filePath;

    public FileService(String filename){
        this.filePath = Path.of(filename);
    }

    public void saveData(Map<UUID, T> data){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))){
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Map<UUID, T> loadData(){
        if (!Files.exists(filePath)) {
            return new HashMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))){
            return (Map<UUID, T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(T entity) {
        Map<UUID, T> data = loadData();
        data.put(entity.getId(),entity);
        saveData(data);
    }

    @Override
    public T read(UUID id) {
        Map<UUID, T> data = loadData();
        T entity = data.get(id);
        if(Objects.isNull(entity)){
            throw new RuntimeException("존재하지 않는 유저입니다");
        }
        return entity;
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, T> data = loadData();
        read(id);
        data.remove(id);
        saveData(data);
    }

    @Override
    public List<T> findAll() {
        Map<UUID, T> data = loadData();
        return new ArrayList<>(data.values());
    }
}
