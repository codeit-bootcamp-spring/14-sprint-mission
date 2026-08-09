package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
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

    public FileUserRepository() {
        //경로 여기서 생성하기
        try {
            Files.createDirectories(Paths.get("./user"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) {


        try (FileOutputStream fos = new FileOutputStream("./user/" + user.getId() + ".ser");
             ObjectOutputStream output = new ObjectOutputStream(fos)) {
            output.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //생 객체를 넣어버려서
    @Override
    public Optional<User> findById(UUID id) {
        try (FileInputStream fis = new FileInputStream("./user/" + id + ".ser");
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
        File[] files = new File("./user").listFiles((dir, name) -> name.endsWith(".ser"));
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
            Files.deleteIfExists(Path.of("./user/" + id + ".ser"));
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
        try (FileOutputStream fos = new FileOutputStream("./user/" + user.getId() + ".ser");
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
