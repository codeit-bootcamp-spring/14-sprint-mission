package com.sprint.mission.discodeit.binarycontent.adapter.storage;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.application.required.BinaryContentStorage;
import com.sprint.mission.discodeit.common.DeletionEvent;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
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
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@ConditionalOnProperty(
    name = "discodeit.storage.type",
    havingValue = "local"
)
@Slf4j
public class LocalBinaryContentStorage implements BinaryContentStorage {


  private final Path root;

  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}")
      String rootPath) {
    this.root = Path.of(rootPath)
        .toAbsolutePath().normalize();
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path path = resolvePath(id);
    try {
      Files.write(
          path,
          bytes,
          StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING,
          StandardOpenOption.WRITE
      );
      return id;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    Path path = resolvePath(id);
    try {
      return Files.newInputStream(
          path, StandardOpenOption.READ
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    InputStream inputStream = get(binaryContentDto.id());
    Resource resource = new InputStreamResource(inputStream);
    ContentDisposition contentDisposition =
        ContentDisposition.attachment()
            .filename(
                binaryContentDto.fileName(),
                StandardCharsets.UTF_8
            ).build();
    return ResponseEntity.ok().contentType(
            MediaType.parseMediaType(
                binaryContentDto.contentType()
            )
        ).contentLength(binaryContentDto.size())
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            contentDisposition.toString()
        ).body(resource);
  }

  @Override
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(DeletionEvent<List<UUID>> deletionEvent) {
    deleteFiles(deletionEvent.getValue());
  }


  private void deleteFiles(List<UUID> attachmentsIds) {
    for (UUID id : attachmentsIds) {
      try {
        Path path = resolvePath(id);
        Files.deleteIfExists(path);
      } catch (IOException e) {
        log.error("파일 삭제 실패 id={}", id, e);
      }
    }
  }


  private Path resolvePath(UUID id) {
    return root.resolve(id.toString()).normalize();
  }
}
