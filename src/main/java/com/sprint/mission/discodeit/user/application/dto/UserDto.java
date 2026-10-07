package com.sprint.mission.discodeit.user.application.dto;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentDto;
import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    boolean online
) {

}
