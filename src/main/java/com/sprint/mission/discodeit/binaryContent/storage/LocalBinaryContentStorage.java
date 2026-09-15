package com.sprint.mission.discodeit.binaryContent.storage;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage{

    private final Path root ;

    public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") Path root){
        this.root = root;
    }

    @PostConstruct
    public void init() throws IOException{
        Files.createDirectories(root);
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        Path path = resolvePath(binaryContentId);
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto dto) {
        InputStream is = get(dto.id());
        Resource resource = new InputStreamResource(is);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + dto.fileName() + "\"")
                .contentType(MediaType.parseMediaType(dto.contentType()))
                .contentLength(dto.size())
                .body(resource);
    }
    
    private Path resolvePath(UUID binaryContentId){
        return root.resolve(binaryContentId.toString());
    }
}
