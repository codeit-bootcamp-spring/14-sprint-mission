package com.sprint.mission.discodeit.message.web.dto.res;

import com.sprint.mission.discodeit.binarycontent.web.dto.res.BinaryContentResponseDTO;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDTO(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UserResponseDTO author,
    List<BinaryContentResponseDTO> attachments
) {
}
