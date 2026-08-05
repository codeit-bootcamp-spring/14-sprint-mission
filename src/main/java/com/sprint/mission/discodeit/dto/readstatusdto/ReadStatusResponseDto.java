package com.sprint.mission.discodeit.dto.readstatusdto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadStatusResponseDto {
    UUID userId;
    UUID channelId;
    Instant lastReadAt;

    UUID id;
    Instant createdAt;
    Instant updatedAt;

    public static ReadStatusResponseDto from(ReadStatus readStatus) {
        return new ReadStatusResponseDto(
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt(),
                readStatus.getId(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt()
        );
    }
}
