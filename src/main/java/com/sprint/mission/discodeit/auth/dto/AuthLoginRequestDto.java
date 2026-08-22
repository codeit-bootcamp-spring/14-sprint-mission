package com.sprint.mission.discodeit.auth.dto;

public record AuthLoginRequestDto(
        String username,
        String password
) {
}
