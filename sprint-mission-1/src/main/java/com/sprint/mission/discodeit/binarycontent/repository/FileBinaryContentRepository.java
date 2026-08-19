package com.sprint.mission.discodeit.binarycontent.repository;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
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
import org.springframework.web.multipart.MultipartFile;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> binaryContentMap = new HashMap<>();
    private final String directory;

    public FileBinaryContentRepository(
        @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        Path path = Paths.get(fileDirectory);
        this.directory = fileDirectory;
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new UncheckedIOException("디렉토리 생성 실패 - path: " + path, e);
        }
        binaryContentLoad();
    }

    private String filePath() {
        return Paths.get(directory, "BinaryContent.ser").toString();
    }

    private void binaryContentLoad() {
        Path file = Paths.get(filePath());
        if (!Files.exists(file)) {
            return;
        }
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
            new FileInputStream(filePath()))) {
            binaryContentMap.putAll((Map<UUID, BinaryContent>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("저장된 데이터를 읽을 수 없습니다. - path: " + filePath(), e);
        }
    }

    private void binaryContentFlush() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
            new FileOutputStream(filePath()))) {
            objectOutputStream.writeObject(binaryContentMap);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다. - path: " + filePath(), e);
        }
    }

    @Override
    public BinaryContent binaryAdd(BinaryContent binaryContent) {
        binaryContentMap.put(binaryContent.getId(), binaryContent);
        binaryContentFlush();
        return binaryContent;
    }

    @Override
    public void delete(UUID binaryId) {
        binaryContentMap.remove(binaryId);
        binaryContentFlush();
    }

    @Override
    public BinaryContent findById(UUID binaryId) {
        return binaryContentMap.get(binaryId);
    }

    @Override
    public List<BinaryContent> findAll() {
        return binaryContentMap.values().stream()
            .toList();
    }

    @Override
    public BinaryContent toBinaryContent(MultipartFile file) {
        try {
            BinaryContent binaryContent = new BinaryContent(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
            );
            return binaryAdd(binaryContent);
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 읽는데 실패했습니다: " + file.getOriginalFilename(), e);
        }
    }
}
