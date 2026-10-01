package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@Entity
@Table(name = "binary_contents")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

    @Column(nullable = false)
    String fileName;

    @Column(nullable = false)
    Long size;

    @Column(nullable = false, length = 100)
    String contentType;

    private BinaryContent(String fileName, Long size, String contentType) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
    }

    public static BinaryContent create(String fileName, Long size, String contentType) {
        return new BinaryContent(fileName, size, contentType);
    }
}
