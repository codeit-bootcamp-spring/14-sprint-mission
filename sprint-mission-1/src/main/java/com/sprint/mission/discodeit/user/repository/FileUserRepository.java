package com.sprint.mission.discodeit.user.repository;

import com.sprint.mission.discodeit.user.entity.User;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

    private final Map<UUID, User> users = new HashMap<>();
    private final String directory;

    public FileUserRepository(
        @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        Path path = Paths.get(fileDirectory);
        this.directory = fileDirectory;
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new UncheckedIOException("디렉토리 생성 실패 - path: " + path, e);
        }
        userLoad();
    }

    private String filePath() {
        return Paths.get(directory, "User.ser").toString();
    }

    private void userLoad() {
        Path file = Paths.get(filePath());
        if (!Files.exists(file)) {
            return;
        }
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
            new FileInputStream(filePath()))) {
            users.putAll((Map<UUID, User>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("기존 유저 데이터가 없습니다. - path: " + filePath(), e);
        }
    }

    private void userFlush() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
            new FileOutputStream(filePath()))) {
            objectOutputStream.writeObject(this.users);
        } catch (IOException e) {
            throw new UncheckedIOException("유저 저장에 실패했습니다. - path: " + filePath(), e);
        }
    }

    @Override
    public User userAdd(User user) {
        this.users.put(user.getId(), user);
        userFlush();
        return user;
    }

    @Override
    public Optional<User> findByUser(UUID userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> findByUserName(String name) {
        return users.values().stream()
            .filter(user -> name.equals(user.getUsername()))
            .findFirst();
    }

    @Override
    public Optional<User> findByUserEmail(String email) {
        return users.values().stream()
            .filter(user -> email.equals(user.getEmail()))
            .findFirst();
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
        userFlush();
    }

    @Override
    public void update(User user) {
        users.replace(user.getId(), user);
        userFlush();
    }

    @Override
    public List<User> findAllUser() {
        return new ArrayList<>(users.values());
    }
}
