package com.sprint.mission.discodeit.user.web.dto.res;

import com.sprint.mission.discodeit.binarycontent.web.dto.res.BinaryContentResponseDTO;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String username,
    String email,
    BinaryContentResponseDTO profile,
    boolean online
) {

}
