package com.sprint.mission.discodeit.binaryContent.domain;

import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String fileName;
    private Long size;
    private String contentType;
    private byte[] bytes;
    private Instant createdAt;

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        if(bytes == null){
            throw new NoSuchElementException();
        }
        this.bytes = bytes;
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

    }

}
