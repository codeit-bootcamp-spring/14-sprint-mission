package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  private UUID id;
  private Instant createdAt;
  //
  private String fileName;
  private Long size;
  @Column(length = 100)
  private String contentType;

  public BinaryContent(String fileName, Long size, String contentType) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    //
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
}
