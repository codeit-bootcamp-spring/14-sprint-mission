package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class FileUserRepository implements UserRepository {
    protected Path filePath = Path.of("user.ser");

    @Override
    public void save(User user) {
        Map<UUID, User> data = loadData();
        data.put(user.getId(),user);
        saveData(data);
    }

    @Override
    public User findById(UUID id) {
        return loadData().get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(loadData().values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, User> data = loadData();
        data.remove(id);
        saveData(data);
    }

    @Override
    public boolean existsByEmail(String email) {
        return  loadData().values().stream()
            .anyMatch(user -> user.getName().equals(email));
    }

    @Override
    public boolean existsByName(String name) {
        return  loadData().values().stream()
            .anyMatch(user -> user.getName().equals(name));
    }

    @Override
    public Optional<User> findByName(String name) {
        return loadData().values().stream()
            .filter(user -> user.getName().equals(name))
            .findFirst();
    }

    public void saveData(Map<UUID,User> data){
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))){
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Map<UUID, User> loadData(){
        if(!Files.exists(filePath))
            return new HashMap<>();
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath.toFile()))){
            return (Map<UUID, User>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
