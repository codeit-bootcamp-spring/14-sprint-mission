package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {

    private final String USER_FILENAME = "users.ser";
    private Map<UUID, User> userMap = loadFile(USER_FILENAME);

    private FileUserRepository() {}

    private static class LazyHolder {
        private static final FileUserRepository INSTANCE = new FileUserRepository();
    }

    public static FileUserRepository getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public User save(User user) {
        userMap.put(user.getId(), user);
        saveToFile(userMap, USER_FILENAME);
        return user;
    }

    @Override
    public User find(UUID id) {
        return Optional.ofNullable(userMap.get(id))
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));
    }

    @Override
    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        if (userMap.remove(id) == null) {
            throw new CustomException(ExceptionType.USER_NOT_FOUND);
        }
        saveToFile(userMap, USER_FILENAME);
    }


    /**
     * Helper
     */

    private Map<UUID, User> loadFile(String filename) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
            return (Map<UUID, User>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToFile(Map<UUID, User> userMap, String filename) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            objectOutputStream.writeObject(userMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
