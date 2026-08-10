package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
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
public class FileMessageRepository implements MessageRepository {

    private final Path directory;

    public FileMessageRepository(
            @Value("${discodeit.repository.file-directory}") String fileDirectory) {
        this.directory = Paths.get(fileDirectory, "message");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Message message) {
        try (FileOutputStream fos = new FileOutputStream(directory.resolve(message.getId() + ".ser").toFile());
             ObjectOutputStream output = new ObjectOutputStream(fos)) {

            output.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //생 객체를 넣어버려서
    @Override
    public Optional<Message> findById(UUID id) {
        try (FileInputStream fis = new FileInputStream(directory.resolve(id + ".ser").toFile());
             ObjectInputStream input = new ObjectInputStream(fis)) {
            return Optional.ofNullable((Message) input.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> findAll() {
        List<Message> lists = new ArrayList<>();
        // 해당 위치 파일 다 긁어 오기
        File[] files = directory.toFile().listFiles((dir, name) -> name.endsWith(".ser"));

        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream input = new ObjectInputStream(fis)) {
                lists.add((Message) input.readObject());
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
    public void deleteByChannelId(UUID channelId) {
        // 삭제할 메세지
        List<UUID> ids = findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(BaseEntity::getId)
                .toList();

        // 진짜 삭제
        for (UUID id : ids) {
            deleteById(id);
        }
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId){

        return findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

}
