package com.sprint.mission.discodeit.userstatus.repository;

import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class FileUserStatusRepository implements UserStatusRepository {

    private final Map<UUID, UserStatus> userStatusMap = new HashMap<>();
    private final String directory;

    public FileUserStatusRepository(
        @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        Path path = Paths.get(fileDirectory);
        this.directory = fileDirectory;
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 생성 실패", e);
        }
        userStatusLoad();
    }

    private String filePath() {
        return Paths.get(directory, "UserStatus.ser").toString();
    }

    private void userStatusLoad() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
            new FileInputStream(filePath()))) {
            userStatusMap.putAll((Map<UUID, UserStatus>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("기존 유저 상태 데이터가 없습니다.");
        }
    }

    private void userStatusFlush() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
            new FileOutputStream(filePath()))) {
            objectOutputStream.writeObject(userStatusMap);
        } catch (IOException e) {
            throw new RuntimeException("유저 상태 저장에 실패했습니다.", e);
        }
    }

    @Override
    public UserStatus statusAdd(UserStatus userStatus) {
        userStatusMap.put(userStatus.getUserStatusId(), userStatus);
        userStatusFlush();
        return userStatusMap.get(userStatus.getUserStatusId());
    }

    @Override
    public void delete(UserStatus userStatus) {
        userStatusMap.remove(userStatus.getUserStatusId());
        userStatusFlush();
    }

    @Override
    public void update(UserStatus userStatus) {
        userStatusMap.replace(userStatus.getUserStatusId(), userStatus);
        userStatusFlush();
    }

    @Override
    public UserStatus findById(UUID userStatusId) {
        return userStatusMap.get(userStatusId);
    }


    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatusMap.values().stream()
            .filter(userStatus -> userStatus.getUserId().equals(userId))
            .findFirst();
    }

    public List<UserStatus> findAll() {
        return userStatusMap.values().stream().toList();
    }
}
