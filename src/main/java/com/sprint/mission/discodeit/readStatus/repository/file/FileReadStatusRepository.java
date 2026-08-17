package com.sprint.mission.discodeit.readStatus.repository.file;

import com.sprint.mission.discodeit.common.entity.BaseEntity;
import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
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
public class FileReadStatusRepository implements ReadStatusRepository {

    private final Path directory;

    public FileReadStatusRepository(
            @Value("${discodeit.repository.file-directory}") String fileDirectory) {
        this.directory = Paths.get(fileDirectory, "readStatus");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(ReadStatus readStatus) {
        try (FileOutputStream fos = new FileOutputStream(directory.resolve(readStatus.getId() + ".ser").toFile());
             ObjectOutputStream output = new ObjectOutputStream(fos)) {
            output.writeObject(readStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //다 찾기
    @Override
    public List<ReadStatus> findAll(){
        List<ReadStatus> lists = new ArrayList<>();
        File[] files = directory.toFile().listFiles((dir, name) -> name.endsWith(".ser"));

        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream input = new ObjectInputStream(fis)) {
                lists.add((ReadStatus) input.readObject());
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        return lists;
    }

    // 채널ID로 유저ID 구하기
    @Override
    public List<UUID> findAllByChannelId(UUID ChannelId){
        return findAll().stream().filter(readStatus -> readStatus.getChannelId().equals(ChannelId))
                .map(ReadStatus::getUserId)
                .toList();
    }

    //channel 아이디로 삭제하기
    @Override
    public void deleteByChannelId(UUID channelId) {
        // 삭제할 메세지
        List<UUID> ids = findAll().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .map(BaseEntity::getId)
                .toList();

        // 진짜 삭제
        for (UUID id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            Files.deleteIfExists(directory.resolve(id + ".ser"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 채널, 유저 같은게 있는가?
    @Override
    public boolean existsByChannelIdAndUserId(UUID ChannelId, UUID userId) {
        return findAll().stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(ChannelId) &&
                        readStatus.getUserId().equals(userId));

    }

    // id로 찾기
    @Override
    public Optional<ReadStatus> findById(UUID id){
        try (FileInputStream fis = new FileInputStream(directory.resolve(id + ".ser").toFile());
             ObjectInputStream input = new ObjectInputStream(fis)) {
            return Optional.ofNullable((ReadStatus) input.readObject());
        } catch (IOException|ClassNotFoundException e) {
            throw new NoSuchElementException();
        }
    }

    // 유저ID로 해당되어있는 ReadStatus 전부 구하기
    @Override
    public List<ReadStatus> findAllByUserId(UUID userId){
        return findAll().stream().filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void update(ReadStatus readStatus) {

        try (FileOutputStream fos = new FileOutputStream(directory.resolve(readStatus.getId() + ".ser").toFile());
             ObjectOutputStream output = new ObjectOutputStream(fos)) {
            output.writeObject(readStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
