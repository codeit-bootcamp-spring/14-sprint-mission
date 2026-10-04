package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage{

    @Value("${discodeit.storage.local.root-path}")
    private String rootPath;

    @PostConstruct
    public void init() {
        try {
            Path root = Path.of(rootPath);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("루트 디렉토리 초기화에 실패했습니다.", e);
        }
    }

    private Path resolvePath(UUID binaryContentId) {
        return Path.of(rootPath, binaryContentId.toString());

    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        log.debug("파일 업로드 시작: binaryContentId={}, size={} bytes", binaryContentId, bytes.length);
        try {
            Path path = resolvePath(binaryContentId);
            Files.write(path, bytes);
            log.info("파일 업로드 완료: binaryContentId={}", binaryContentId);
            return binaryContentId;
        } catch (IOException e) {
            log.error("파일 업로드 실패: binaryContentId={}", binaryContentId, e);
            throw new UncheckedIOException("파일 저장에 실패했습니다.", e);
        }
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        log.debug("파일 다운로드 요청: binaryContentId={}", binaryContentId);
        Path path = resolvePath(binaryContentId);
        if (!Files.exists(path)) {
            log.warn("존재하지 않는 파일 다운로드 시도: binaryContentId={}", binaryContentId);
            throw new NoSuchElementException("파일을 찾을 수 없습니다: " + binaryContentId);
        }
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            log.error("파일 다운로드 실패: binaryContentId={}", binaryContentId, e);
            throw new UncheckedIOException("파일 읽기에 실패했습니다.", e);
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentResponseDto binaryContentDto) {
        log.debug("파일 다운로드 시작 : binaryContentId={}", binaryContentDto.id());
        InputStream inputStream = get(binaryContentDto.id());
        Resource resource = new InputStreamResource(inputStream);

        log.info("파일 다운로드 완료: binaryContentId={}", binaryContentDto.id());

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
            .body(resource);
    }

    @Override
    public void delete(UUID binaryContentId) {
        log.debug("파일 삭제 시작: binaryContentId={}", binaryContentId);
        Path path = resolvePath(binaryContentId);
        try {
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                log.info("파일 삭제 완료: binaryContentId={}", binaryContentId);
            } else {
                log.warn("삭제하려는 파일이 이미 존재하지 않음: binaryContentId={}", binaryContentId);
            }
        } catch (IOException e) {
            log.error("파일 삭제 실패: binaryContentId={}", binaryContentId, e);
            throw new UncheckedIOException("파일 삭제 실패: "+ binaryContentId, e);
        }
    }
}
