package com.sprint.mission.discodeit.binaryContent.repository.file;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    private final Path directory;

    public FileBinaryContentRepository(
            @Value("${discodeit.repository.file-directory}") String fileDirectory) {
        this.directory = Path.of(fileDirectory, "binaryContent");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(BinaryContent binaryContent) {
        try (FileOutputStream fos = new FileOutputStream(directory.resolve(binaryContent.getId() + ".ser").toFile());
             ObjectOutputStream output = new ObjectOutputStream(fos)) {
            output.writeObject(binaryContent);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        try (FileInputStream fis = new FileInputStream(directory.resolve(id + ".ser").toFile());
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
        File[] files = directory.toFile().listFiles((dir, name) -> name.endsWith(".ser"));

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
            Files.deleteIfExists(directory.resolve(id + ".ser"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
