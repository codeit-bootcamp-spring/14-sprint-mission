package com.sprint.mission.discodeit.web.controller.dto.res;

import com.sprint.mission.discodeit.domain.entity.ReadStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReadStatusResponseDTO(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt,
    Instant createdAt,
    Instant updatedAt
){
    public static ReadStatusResponseDTO from(ReadStatus readStatus){
        return new ReadStatusResponseDTO(
            readStatus.getId(),
            readStatus.getUserId(),
            readStatus.getChannelId(),
            readStatus.getLastReadAt(),
            readStatus.getCreatedAt(),
            readStatus.getUpdatedAt()
        );
    }

    public static List<ReadStatusResponseDTO> fromList(List<ReadStatus> readStatusList){
        return readStatusList.stream()
            .map(ReadStatusResponseDTO::from)
            .toList();
    }
}
