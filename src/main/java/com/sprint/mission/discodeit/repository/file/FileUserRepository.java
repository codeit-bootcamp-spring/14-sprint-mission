package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.user.UserDto;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static final Map<UUID, User> EMPTY_BUFFER = new HashMap<>();
    private static final File USER_FILE = new File("src/main/java/com/sprint/mission/discodeit/repository/file/user.ser");
    private static Map<UUID, User> buffer = EMPTY_BUFFER;

    static {
        if (!USER_FILE.exists() || USER_FILE.length() == 0) {
            writeFile();
        }
        readFromFile();
    }

    @Override
    public User create(User user) {
        UUID userId = user.getId();
        return findById(userId).orElseGet(() -> {
            buffer.put(userId, user);
            writeFile();
            return user;
        });
    }

    @Override
    public Optional<User> findById(UUID id) {
        readFromFile();
        return Optional.ofNullable(buffer.get(id));
    }

    @Override
    public List<User> findAll() {
        readFromFile();
        return new ArrayList<>(buffer.values());
    }

    @Override
    public void update(UUID id, UserDto dto) {
        findById(id).ifPresent(retrieved -> {
            retrieved.update(dto);
            writeFile();
        });
    }

    @Override
    public void deleteById(UUID id) {
        findById(id).ifPresent(retrieved -> {
            buffer.remove(id);
            writeFile();
        });
    }

    private static void readFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            Map<UUID, User> retrieved = (Map<UUID, User>) inputStream.readObject();
            buffer = new HashMap<>(retrieved);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            outputStream.writeObject(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
