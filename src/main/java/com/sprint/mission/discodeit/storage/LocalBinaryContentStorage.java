package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * 파일 경로 규칙은 {root}/{BinaryContent id}..
 * discodeit.storage.type=local 일 때만 bean으로 등록.
 */
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {
    private final Path root;

    // application.yaml의 discodeit.storage.local.root-path 값을 스프링이 자동으로 넣어준다.
    public LocalBinaryContentStorage(
        @Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Path.of(rootPath);
        }

    //루트 디렉토리를 준비한다.
    @PostConstruct
    public void init() {
        if (Files.notExists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to initialize storage root directory", e);
            }
        }
    }

    //파일의 실제 저장 위치에 대한 규칙을 정의
    //put,get이 전부 같은 규칙으로 경로를 계산하도록 한 곳에 모은다.
    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        Path path = resolvePath(id);
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write binary content: " + id, e);
        }
        return id;
    }

    @Override
    public InputStream get(UUID id) {
        Path path = resolvePath(id);
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read binary content: " + id,e);
        }
    }

    //get 메소드를 통해 파일의 바이너리 데이터를 조회한다.
    //BinaryContentDto와 바이너리 데이터를 활용해 ResponseEntity<Resource> 응답을 생성 후 반환한다.
    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        InputStream inputStream = get(binaryContentDto.id()); //"get 메소드를 통해 바이너리 데이터를 조회"
        Resource resource = new InputStreamResource(inputStream);

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(binaryContentDto.fileName())  //"BinaryContentDto"를 활용
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
                .contentLength(binaryContentDto.size())
                .body(resource); //"ResponseEntity<Resource> 응답을 생성 후 반환"
    }
}
