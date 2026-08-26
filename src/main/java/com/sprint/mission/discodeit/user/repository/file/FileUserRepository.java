package com.sprint.mission.discodeit.user.repository.file;

import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

    private final Path directory;

    public FileUserRepository(
            @Value("${discodeit.repository.file-directory}") String fileDirectory){
        this.directory = Paths.get(fileDirectory, "user");
        try{
            Files.createDirectories(directory);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) {


        try (FileOutputStream fos = new FileOutputStream(directory.resolve(user.getId() + ".ser").toFile());
             ObjectOutputStream output = new ObjectOutputStream(fos)) {
            output.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //생 객체를 넣어버려서
    @Override
    public Optional<User> findById(UUID id) {
        try (FileInputStream fis = new FileInputStream(directory.resolve(id + ".ser").toFile());
             ObjectInputStream input = new ObjectInputStream(fis)) {
            return Optional.ofNullable((User) input.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> lists = new ArrayList<>();
        // 해당 위치 파일 다 긁어 오기
        File[] files = directory.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        if (files == null) {
            return List.of();
        }

        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream input = new ObjectInputStream(fis)) {
                lists.add((User) input.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


        return lists;
    }

    @Override
    public void deleteById(UUID id) {
        try {
            Files.deleteIfExists(directory.resolve(id + ".ser"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream().filter(user -> user.getUserName().equals(username))
                .findFirst();
    }

    @Override
    public void update(User user) {
        try (FileOutputStream fos = new FileOutputStream(directory.resolve(user.getId() + ".ser").toFile());
             ObjectOutputStream output = new ObjectOutputStream(fos)) {
            output.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findAll().stream().filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
