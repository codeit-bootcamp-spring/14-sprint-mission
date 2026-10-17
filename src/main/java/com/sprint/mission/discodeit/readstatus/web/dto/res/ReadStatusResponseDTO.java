package com.sprint.mission.discodeit.readstatus.web.dto.res;

import com.sprint.mission.discodeit.readstatus.domain.entity.ReadStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReadStatusResponseDTO(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
){

}
