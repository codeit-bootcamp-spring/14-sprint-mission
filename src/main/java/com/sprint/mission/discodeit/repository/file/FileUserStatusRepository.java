package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class FileUserStatusRepository implements UserStatusRepository {

    protected Path filePath = Path.of("userStatus.ser");

    @Override
    public void save(UserStatus userStatus) {
        Map<UUID, UserStatus> data = loadData();
        data.put(userStatus.getId(), userStatus);
        saveData(data);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return loadData().values().stream()
            .filter(userStatus -> userStatus.getUserId().equals(userId))
            .findFirst();
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, UserStatus> data = loadData();
        data.remove(id);
        saveData(data);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return loadData().values().stream()
            .anyMatch(userStatus -> userStatus.getUserId().equals(userId));
    }

    @Override
    public UserStatus findById(UUID id) {
        return loadData().get(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return loadData().values().stream().toList();
    }
    public void saveData(Map<UUID, UserStatus> data){
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))){
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Map<UUID, UserStatus> loadData(){
        if(!Files.exists(filePath))
            return new HashMap<>();
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath.toFile()))){
            return (Map<UUID, UserStatus>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
