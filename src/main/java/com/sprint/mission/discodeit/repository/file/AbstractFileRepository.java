package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.repository.FileRepository;
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

public abstract class AbstractFileRepository<T extends BaseEntity> implements FileRepository<T> {
    protected final String filePath;

    public AbstractFileRepository(String filePath) {
        this.filePath = filePath;
    }

    protected Map<UUID, T> loadData() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // 2. 공통 파일 쓰기 로직
    protected void saveData(Map<UUID, T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 3. 공통 CRUD 구현 (모든 자식 레포지토리가 이 로직을 그대로 물려받습니다)
    @Override
    public T save(T entity) {
        Map<UUID, T> data = loadData();
        data.put(entity.getId(), entity); // 각 엔티티의 ID를 꺼내서 저장
        saveData(data);
        return entity;
    }

    @Override
    public Optional<T> findById(UUID id) {
        Map<UUID, T> data = loadData();
        return Optional.ofNullable(data.get(id)); // 데이터가 없으면 null 반환
    }

    @Override
    public List<T> findAll() {
        Map<UUID, T> data = loadData();
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, T> data = loadData();
        data.remove(id);
        saveData(data);
    }

    @Override
    public void deleteAll(){
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
        }
    }
}