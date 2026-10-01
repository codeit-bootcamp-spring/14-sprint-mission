package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

@Component
@ConditionalOnProperty(
    name = "discodeit.storage.type",
    havingValue = "local"
)
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
            throw new RuntimeException("로컬 저장소 루트 디렉토리 초기화에 실패했습니다.", e);
        }
    }

    @Override
    public UUID put(UUID id, byte[] data) {
        try {
            Path filePath = resolvePath(id);
            Files.write(filePath, data);
            return id;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다 : " + id, e);
        }
    }

    @Override
    public InputStream get(UUID id) {
        try {
            Path filePath = resolvePath(id);
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽어올 수 없습니다: " + id, e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto dto) {
        InputStream inputStream = get(dto.id());
        Resource resource = new InputStreamResource(inputStream);

        String encodedFileName = UriUtils.encode(dto.fileName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(dto.contentType()))
            .contentLength(dto.size())
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
            .body(resource);
    }

    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }
}
