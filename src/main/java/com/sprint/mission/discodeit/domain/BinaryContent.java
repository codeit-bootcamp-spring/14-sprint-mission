package com.sprint.mission.discodeit.domain;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false, length = 255)
  String fileName;
  @Column(name = "size", nullable = false)
  Long size;
  @Column(name = "content_type", nullable = false, length = 100)
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
