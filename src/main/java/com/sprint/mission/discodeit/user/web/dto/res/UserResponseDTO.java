package com.sprint.mission.discodeit.user.web.dto.res;

import com.sprint.mission.discodeit.binarycontent.web.dto.res.BinaryContentResponseDTO;
import com.sprint.mission.discodeit.user.domain.entity.Role;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String username,
    String email,
    Role role,
    BinaryContentResponseDTO profile,
    boolean online
) {

}
