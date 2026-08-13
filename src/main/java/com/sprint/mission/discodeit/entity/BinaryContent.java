package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    // 공통 필드(수정 불가능 객체라서 update 삭제함.)
    private UUID id;
    private Instant createdAt;

    // [이미지, 파일](파일명 & 확장자 종류가 필드) 등 바이너리 데이터(필드)를 표현하는 도메인 모델
    // 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용
    // 참조 필드
    private byte[] bytes;
    private String fileName;
    private String contentType;

    public BinaryContent(byte[] bytes, String fileName, String contentType) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.bytes = bytes;
        this.fileName = fileName;
        this.contentType = contentType;
    }
}
