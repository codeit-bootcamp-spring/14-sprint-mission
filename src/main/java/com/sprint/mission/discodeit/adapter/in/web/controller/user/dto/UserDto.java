package com.sprint.mission.discodeit.adapter.in.web.controller.user.dto;

import com.sprint.mission.discodeit.adapter.in.web.controller.binaryContent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;

import java.util.UUID;

public record UserDto(UUID id,
                      String username,
                      String email,
                      BinaryContentResponseDto profile,
                      Boolean online,
                      String role) {

    public static UserDto from(UserResult result) {
        return new UserDto(
                result.id(),
                result.username(),
                result.email(),
                BinaryContentResponseDto.from(result.binaryContentResult()),
                result.isOnline(),
                result.role()
        );
    }
}
