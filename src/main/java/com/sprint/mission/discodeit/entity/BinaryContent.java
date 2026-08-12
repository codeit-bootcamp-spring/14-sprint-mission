package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createAt;
    private String fileName;
    private Integer fileSize;

    public BinaryContent(String fileName, Integer fileSize) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
}
