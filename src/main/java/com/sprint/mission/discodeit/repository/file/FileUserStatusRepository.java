package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.NoSuchElementException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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
public class FileUserStatusRepository implements UserStatusRepository {

    @Override
    public void save(UserStatus userStatus) {
        try {
            Files.createDirectories(Paths.get("./userStatus"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileOutputStream fos = new FileOutputStream("./userStatus/" + userStatus.getId() + ".ser");
             ObjectOutputStream output = new ObjectOutputStream(fos)) {

            output.writeObject(userStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        // 유저아이디로 찾는다.
        UserStatus userStatus = findAll().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst()
                .orElseThrow();

        //진짜 삭제
        deleteById(userStatus.getId());
    }

    @Override
    public List<UserStatus> findAll() {
        List<UserStatus> lists = new ArrayList<>();
        File[] files = new File("./userStatus").listFiles((dir, name) -> name.endsWith(".ser"));
        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream input = new ObjectInputStream(fis)) {
                lists.add((UserStatus) input.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return lists;
    }

    @Override
    public void deleteById(UUID id) {
        try {
            Files.deleteIfExists(Path.of("./userStatus/" + id + ".ser"));
        } catch (IOException e) {
            throw new NoSuchElementException();
        }
    }


    @Override
    public Optional<UserStatus> findById(UUID id) {
        try (FileInputStream fis = new FileInputStream("./userStatus/" + id + ".ser");
             ObjectInputStream input = new ObjectInputStream(fis)) {
            return Optional.ofNullable((UserStatus) input.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public Optional<UserStatus> update(UserStatus userStatus) {
        try (FileOutputStream fos = new FileOutputStream("./userStatus/" + userStatus.getId() + ".ser");
             ObjectOutputStream output = new ObjectOutputStream(fos)) {
            output.writeObject(userStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.of(userStatus);
    }

}
