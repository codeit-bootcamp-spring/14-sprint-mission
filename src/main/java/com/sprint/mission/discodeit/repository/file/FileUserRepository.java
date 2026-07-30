package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private static final String FILE_NAME = "users.dat";

    private Map<UUID, User> loadData() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, User> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생하였습니다.");
        }
    }

    @Override
    public User save(User user) {
        Map<UUID, User> data = loadData();
        data.put(user.getId(), user);

        saveData(data);

        return user;
    }

    @Override
    public User findById(UUID id) {
        return loadData().get(id);
    }

    @Override
    public List<User> findAll() {
        return loadData().values()
                .stream()
                .toList();
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> data = loadData();
        data.remove(id);
        
        saveData(data);
    }
}
