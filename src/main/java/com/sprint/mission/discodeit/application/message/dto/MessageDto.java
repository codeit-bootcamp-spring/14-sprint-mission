package com.sprint.mission.discodeit.application.message.dto;

import com.sprint.mission.discodeit.application.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.application.user.dto.UserDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UserDto author,
    List<BinaryContentDto> attachments
) {


}
