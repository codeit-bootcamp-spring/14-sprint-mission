package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * 프로필 이미지와 메시지 첨부파일로 쓰는 바이너리 데이터.
 *
 * Common을 상속하지 않는다. 수정 불가능한 도메인이라 updatedAt이 없어야 하는데,
 * 상속하면 딸려오기 때문..
 *
 * 참조는 한 방향이다. User와 Message가 이 id를 갖고, 여기서는 되참조하지 않는다.
 * 되참조하면 지울 때 양쪽을 맞춰야 한다.
 */
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private final String fileName;
    private final String contentType;
    private final Long size;
    private final byte[] bytes;

    public BinaryContent(String fileName, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
        this.size = bytes == null ? 0L : (long) bytes.length;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getSize() {
        return size;
    }

    public byte[] getBytes() {
        return bytes;
    }

}
