package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private UUID id;
    private Instant createdAt;
    private String fileName; //파일명
    private String contentType; //파일의 타입
    private Long size; //파일크기
    private byte[] bytes; //업로드 및 다운로드를 위한 바이트로 변환

    public BinaryContent(UUID id, Instant createdAt, String fileName, String contentType, Long size, byte[] bytes) {
        this.id = id;
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.bytes = bytes.clone();
    }

    public byte[] getBytes() {
        return bytes.clone();
    }
}
