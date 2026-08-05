package com.sprint.mission.discodeit.dto.binarycontentdto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BinaryContentResponseDto {
    String contentAddress;
    UUID userId;
    UUID messageId;

    UUID id;
    Instant createdAt;

    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
                binaryContent.getContentAddress(),
                binaryContent.getUserId(),
                binaryContent.getMessageId(),
                binaryContent.getId(),
                binaryContent.getCreatedAt());
    }
}
