package com.sprint.mission.discodeit.application.user.dto;

import com.sprint.mission.discodeit.application.binarycontent.dto.BinaryContentDto;
import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    boolean online
) {

}
