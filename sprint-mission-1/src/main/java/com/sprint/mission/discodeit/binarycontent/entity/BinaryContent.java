package com.sprint.mission.discodeit.binarycontent.entity;

import jakarta.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BinaryContent implements Serializable {

    // 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
    // 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용
    // 수정 불가능한 도메인 모델로 간주
    // 따라서 updatedAt 필드는 정의하지 않습니다.
    // User, Message 도메인 모델과의 의존 관계 방향성을 잘 고려하여 id 참조 필드를 추가
    // 유저에서 파일 UUID 갖기
    // 메시지에서 파일 List<UUID> 갖기
    @Serial
    private static final long serialVersionUID = 1L;
    UUID binaryContentId = UUID.randomUUID();
    Instant createdAt = Instant.now();
    @Nullable
    private String fileName;
    private String contentType;
    private byte[] bytes;
}
