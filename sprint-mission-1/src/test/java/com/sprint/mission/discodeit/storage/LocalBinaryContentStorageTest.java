package com.sprint.mission.discodeit.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.storage.LocalBinaryContentStorage;
import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ExceptionType;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class LocalBinaryContentStorageTest {

    @TempDir
    Path root;

    private LocalBinaryContentStorage storage;

    @BeforeEach
    void setUp() {
        storage = new LocalBinaryContentStorage(root);
        storage.init();
    }

    @Test
    @DisplayName("저장한 파일을 다운로드하면 200과 저장한 내용을 반환한다")
    void downloadSavedFile() throws Exception {
        UUID id = UUID.randomUUID();
        byte[] bytes = "hello".getBytes(StandardCharsets.UTF_8);
        storage.put(id, bytes);

        ResponseEntity<Resource> response =
            storage.download(new BinaryContentDto(id, "hello.txt", (long) bytes.length, "text/plain"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        try (var in = response.getBody().getInputStream()) {
            assertThat(in.readAllBytes()).isEqualTo(bytes);
        }
    }

    @Test
    @DisplayName("파일이 없으면 404 FILE_NOT_FOUND 예외를 던진다")
    void downloadMissingFile() {
        BinaryContentDto dto = new BinaryContentDto(UUID.randomUUID(), "none.txt", 0L, "text/plain");

        assertThatThrownBy(() -> storage.download(dto))
            .isInstanceOf(DiscodeitException.class)
            .extracting(e -> ((DiscodeitException) e).getType())
            .isEqualTo(ExceptionType.FILE_NOT_FOUND);
        assertThat(ExceptionType.FILE_NOT_FOUND.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("파일은 있지만 읽기 권한이 없으면 STORAGE_UNAVAILABLE 예외를 던진다")
    void downloadUnreadableFile() throws Exception {
        UUID id = UUID.randomUUID();
        Path file = Files.writeString(root.resolve(id.toString()), "secret");
        file.toFile().setReadable(false);
        assumeFalse(Files.isReadable(file), "root 권한에서는 읽기 권한 제거를 확인할 수 없음");
        BinaryContentDto dto = new BinaryContentDto(id, "secret.txt", 6L, "text/plain");

        assertThatThrownBy(() -> storage.download(dto))
            .isInstanceOf(DiscodeitException.class)
            .extracting(e -> ((DiscodeitException) e).getType())
            .isEqualTo(ExceptionType.STORAGE_UNAVAILABLE);
    }
}
