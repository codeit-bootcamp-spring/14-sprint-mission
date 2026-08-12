package com.sprint.mission.discodeit.readstatus.repository;

import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> readStatusMap = new HashMap<>();
    private final String directory;

    public FileReadStatusRepository(
        @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        Path path = Paths.get(fileDirectory);
        this.directory = fileDirectory;
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new UncheckedIOException("디렉토리 생성 실패 - path: " + path, e);
        }
        readStatusLoad();
    }

    private String filePath() {
        return Paths.get(directory, "ReadStatus.ser").toString();
    }

    private void readStatusLoad() {
        Path file = Paths.get(filePath());
        if (!Files.exists(file)) {
            return;
        }
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
            new FileInputStream(filePath()))) {
            readStatusMap.putAll((Map<UUID, ReadStatus>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("기존 채널 상태 데이터가 없습니다. - path: " + filePath(), e);
        }
    }

    private void readStatusFlush() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
            new FileOutputStream(filePath()))) {
            objectOutputStream.writeObject(readStatusMap);
        } catch (IOException e) {
            throw new UncheckedIOException("채널 상태 저장에 실패했습니다. - path: " + filePath(), e);
        }
    }

    @Override
    public ReadStatus statusAdd(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
        readStatusFlush();
        return readStatus;
    }

    @Override
    public ReadStatus findById(UUID readStatusId) {
        return readStatusMap.get(readStatusId);
    }

    @Override
    public List<UUID> findByChannelId(UUID channelId) {
        return readStatusMap.values().stream()
            .filter(readStatus -> readStatus.getChannelId().equals(channelId))
            .map(ReadStatus::getUserId)
            .toList();
    }

    @Override
    public List<UUID> findByUserId(UUID userId) {
        return readStatusMap.values().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId))
            .map(ReadStatus::getChannelId)
            .toList();
    }

    @Override
    public List<ReadStatus> findByAllUserList(UUID userId) {
        return readStatusMap.values().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId))
            .toList();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<UUID> readStatusId = readStatusMap.values().stream()
            .filter(readStatus -> readStatus.getChannelId().equals(channelId))
            .map(ReadStatus::getId)
            .toList();

        readStatusId.forEach(readStatusMap::remove);
        readStatusFlush();
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusMap.remove(readStatusId);
        readStatusFlush();
    }

    @Override
    public void update(ReadStatus readStatus) {
        readStatusMap.replace(readStatus.getId(), readStatus);
        readStatusFlush();
    }
}
