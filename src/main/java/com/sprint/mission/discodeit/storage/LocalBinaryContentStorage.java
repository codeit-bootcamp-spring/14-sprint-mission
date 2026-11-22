package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.binarycontent.FileStorageException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
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
            log.error("파일 저장 폴더 생성 실패: {}", root, e);
            throw new FileStorageException(root.toString());
        }
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        log.debug("파일 업로드 시작: binaryContentId={}, size={}", binaryContentId, bytes.length);
        try {
            Files.write(resolvePath(binaryContentId), bytes);
            log.info("파일 업로드 완료: binaryContentId={}", binaryContentId);
            return binaryContentId;
        } catch (IOException e) {
            log.error("파일 업로드 실패: binaryContentId={}", binaryContentId, e);
            throw new FileStorageException(binaryContentId.toString());
        }
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        if (Files.notExists(path)) {
            log.warn("저장된 파일이 없음: binaryContentId={}", binaryContentId);
            throw new BinaryContentNotFoundException(binaryContentId);
        }
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            log.error("파일 읽기 실패: binaryContentId={}", binaryContentId, e);
            throw new FileStorageException(binaryContentId.toString());
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        log.debug("파일 다운로드 시작: binaryContentId={}", binaryContentDto.id());
        Resource resource = new InputStreamResource(get(binaryContentDto.id()));

        ContentDisposition contentDisposition = ContentDisposition.attachment()
            .filename(binaryContentDto.fileName(), StandardCharsets.UTF_8)
            .build();

        log.info("파일 다운로드 응답 준비 완료: binaryContentId={}, fileName={}",
            binaryContentDto.id(), binaryContentDto.fileName());
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
