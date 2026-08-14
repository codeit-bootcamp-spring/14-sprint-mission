package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Getter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;

    private final String fileName;
    private final byte[] bytes;


    private BinaryContent(String fileName, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.bytes = bytes;
    }

    public static BinaryContent create(MultipartFile multipartFile) {
        BinaryContent newBinaryContent = null;

        try {
            newBinaryContent = new BinaryContent(
                    multipartFile.getOriginalFilename(),
                    multipartFile.getBytes()
            );
        } catch (Exception e) {
            log.warn("엥? Binary Content 변환 안됨");
        }

        return newBinaryContent;
    }

    public byte[] getBytes() {
        return this.bytes.clone();
    }
}
