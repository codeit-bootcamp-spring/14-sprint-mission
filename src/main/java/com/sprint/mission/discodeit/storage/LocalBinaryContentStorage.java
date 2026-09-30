package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.binarycontent.web.dto.res.BinaryContentResponseDTO;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage{
    private final Path path;

    public LocalBinaryContentStorage(@Value("${file.upload-dir}") String fileDir) {
        this.path = Path.of(fileDir).toAbsolutePath().normalize();
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        Path filePath = resolvePath(id);

        try(OutputStream outputStream = Files.newOutputStream(filePath))
        {
            outputStream.write(bytes);
            return id;
        }catch (IOException e){
            log.error("파일 저장 실패", e);
            //deleteIfExists(filePath);
            throw new CustomException(CustomErrorCode.FILE_STORE_FAILED);
        }
    }

    @Override
    public InputStream get(UUID id) {
        Path filePath = resolvePath(id);

        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            log.error("파일 조회 실패", e);
            throw new CustomException(CustomErrorCode.FILE_NOT_FOUND);
        }
    }

    //void init();

    private Path resolvePath(UUID id){
        return path.resolve(id.toString());
    }

    private void deleteIfExists(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("실패하고 또 실패", e);
        }
    }
}
