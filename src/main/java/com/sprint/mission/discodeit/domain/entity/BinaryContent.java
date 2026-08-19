package com.sprint.mission.discodeit.domain.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BinaryContent {
    @Builder.Default
    UUID id = UUID.randomUUID();

    String pathUrl;
    String fileName;
    String fileType;

    @Builder.Default
    Instant createdAt = Instant.now();

    static public BinaryContent init(String pathUrl, String fileName, String fileType){
        return BinaryContent.builder().pathUrl(pathUrl).fileName(fileName).fileType(fileType).build();
    }
}
