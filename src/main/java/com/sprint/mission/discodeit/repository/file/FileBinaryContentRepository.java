package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {

    @Override
    public void save(BinaryContent binaryContent) {
        try{
            Files.createDirectories(Path.of("./binaryContent"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (FileOutputStream fos = new FileOutputStream("./binaryContent/" + binaryContent.getId()+".ser");
             ObjectOutputStream output = new ObjectOutputStream(fos)) {
            output.writeObject(binaryContent);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        try (FileInputStream fis = new FileInputStream("./binaryContent/" + id +".ser");
             ObjectInputStream input = new ObjectInputStream(fis)) {
            return Optional.ofNullable((BinaryContent) input.readObject());
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<BinaryContent> findAll() {
        List<BinaryContent> lists = new ArrayList<>();
        // 해당 위치 파일 다 긁어 오기
        File[] files = new File("./binaryContent").listFiles((dir, name) -> name.endsWith(".ser"));

        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream input = new ObjectInputStream(fis)) {
                lists.add((BinaryContent)input.readObject());
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        return lists;
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return findAll().stream()
                .filter(binaryContent -> ids.contains(binaryContent.getId()))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        try{
            Files.deleteIfExists(Path.of("./binaryContent/" + id + ".ser"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
