package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.common.exception.binarycontent.FileError;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    name = "discodeit.storage.type",
    havingValue = "local"
)
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;

    public LocalBinaryContentStorage(
        @Value("${discodeit.storage.local.root-path}") Path root) {
        this.root = root;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new FileError();
        }
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        try {
            Files.write(resolvePath(id), bytes);
        } catch (IOException e) {
            throw new FileError();
        }

        return id;
    }
    @Override
    public InputStream get(UUID id) {
        try {
            return new BufferedInputStream(
                new FileInputStream(resolvePath(id).toFile())
            );
        } catch (IOException e) {
            throw new FileError();
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto dto) {
        MediaType contentType;

        if (dto.contentType() == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        } else {
            contentType = MediaType.parseMediaType(dto.contentType());
        }

        String disposition = ContentDisposition.attachment()
            .filename(dto.fileName(), StandardCharsets.UTF_8)
            .build()
            .toString();

        return ResponseEntity.ok()
            .contentType(contentType)
            .contentLength(dto.size())
            .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
            .body(new InputStreamResource(get(dto.id())));
    }

    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }
}
