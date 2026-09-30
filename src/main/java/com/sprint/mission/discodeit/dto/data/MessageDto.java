package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
    UUID id,
    Instant createdAt,  //프론트와 이름을 맞추기
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId,
    List<BinaryContentDto> attachmentIds
) {

}
