package com.sprint.mission.discodeit.user.repository.file;

import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {

    private final Path directory;

    public FileUserStatusRepository(
            @Value("${discodeit.repository.file-directory}") String fileDirectory) {
        this.directory = Paths.get(fileDirectory, "userStatus");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(UserStatus userStatus) {
        try (FileOutputStream fos = new FileOutputStream(directory.resolve(userStatus.getId() + ".ser").toFile());
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
        File[] files = directory.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream input = new ObjectInputStream(fis)) {
                lists.add((UserStatus) input.readObject());
            } catch (IOException | ClassNotFoundException e) {
                log.error("UserStatus findAll error", e);
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
            throw new NoSuchElementException();
        }
    }


    @Override
    public Optional<UserStatus> findById(UUID id) {
        try (FileInputStream fis = new FileInputStream(directory.resolve(id + ".ser").toFile());
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
        try (FileOutputStream fos = new FileOutputStream(directory.resolve(userStatus.getId() + ".ser").toFile());
             ObjectOutputStream output = new ObjectOutputStream(fos)) {
            output.writeObject(userStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.of(userStatus);
    }

}
