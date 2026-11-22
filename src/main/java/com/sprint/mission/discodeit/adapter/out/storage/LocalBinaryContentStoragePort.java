package com.sprint.mission.discodeit.adapter.out.storage;

import com.sprint.mission.discodeit.core.application.binarycontent.port.out.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.application.binarycontent.port.out.dto.response.DownloadContent;
import com.sprint.mission.discodeit.core.common.log.MutationLog;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "discodeit.storage.type",
        havingValue = "local"
)
public class LocalBinaryContentStoragePort implements BinaryContentStoragePort {
    private final Path root;

    public LocalBinaryContentStoragePort(
            @Value(value = "${discodeit.storage.local.root-path}") Path root
    ) {
        this.root = root;
    }

    @PostConstruct
    public void init() {
        if (Files.exists(root)) {
            return;
        }

        try {
            Files.createDirectory(root);
            log.info("LocalStorage 경로 디렉토리 생성 완료");
        } catch (IOException e) {
            throw new StorageException(StorageExceptionType.LOCAL_STORAGE_DIRECTORY_CREATION_FAILED, Collections.emptyMap(), e);
        }
    }

    @MutationLog
    @Override
    public UUID put(UUID id, byte[] bytes) {
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            throw new StorageException(StorageExceptionType.LOCAL_STORAGE_FILE_ALREADY_EXISTS, Collections.emptyMap());
        }

        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new StorageException(StorageExceptionType.LOCAL_STORAGE_FILE_CREATION_FAILED, Collections.emptyMap(), e);
        }
        return id;
    }

    @Override
    public InputStream get(UUID id) {
        Path path = resolvePath(id);
        if (Files.notExists(path)) {
            throw new StorageException(StorageExceptionType.LOCAL_STORAGE_FILE_NOT_FOUND, Collections.emptyMap());
        }

        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new StorageException(StorageExceptionType.LOCAL_STORAGE_FILE_READ_FAILED, Collections.emptyMap(), e);
        }
    }

    @Override
    public DownloadContent download(UUID id) {
        return new DownloadContent.Stream(get(id));
    }

    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }

}
