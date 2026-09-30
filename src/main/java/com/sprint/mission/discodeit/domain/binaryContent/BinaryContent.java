package com.sprint.mission.discodeit.domain.binaryContent;

import com.sprint.mission.discodeit.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.AccessLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
 *
 */
@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {
    @Column(length = 255, nullable = false)
    private String fileName;
    @Column(nullable = false)
    private Long size;
    @Column(length = 100, nullable = false)
    private String contentType;

    private BinaryContent(String fileName, String contentType, byte[] bytes) {
        super();
        this.fileName = fileName;
        this.size = (long) bytes.length;
        this.contentType = contentType;
    }

    public static BinaryContent of(String fileName, String contentType, byte[] bytes) {
        return new BinaryContent(fileName, contentType, bytes);
    }
}
