package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;

    public LocalBinaryContentStorage(
        @Value("${discodeit.storage.local.root-path}") Path root
    ) {
        this.root = root;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new IllegalStateException("저장소 폴더를 만들 수 없습니다: " + root, e);
        }
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        try {
            Files.write(resolvePath(binaryContentId), bytes);
            return binaryContentId;
        } catch (IOException e) {
            throw new DiscodeitException(ErrorCode.FILE_STORAGE_ERROR,
                "파일 저장에 실패했습니다. id: " + binaryContentId);
        }
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        if (Files.notExists(path)) {
            throw new DiscodeitException(ErrorCode.BINARY_CONTENT_NOT_FOUND,
                "저장된 파일이 없습니다. id: " + binaryContentId);
        }
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new DiscodeitException(ErrorCode.FILE_STORAGE_ERROR,
                "파일을 읽을 수 없습니다. id: " + binaryContentId);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        Resource resource = new InputStreamResource(get(binaryContentDto.id()));

        ContentDisposition contentDisposition = ContentDisposition.attachment()
            .filename(binaryContentDto.fileName(), StandardCharsets.UTF_8)
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
            .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
            .contentLength(binaryContentDto.size())
            .body(resource);
    }

    private Path resolvePath(UUID binaryContentId) {
        return root.resolve(binaryContentId.toString());
    }
}
