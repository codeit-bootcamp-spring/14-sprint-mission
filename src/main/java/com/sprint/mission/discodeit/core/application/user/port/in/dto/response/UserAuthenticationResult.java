package com.sprint.mission.discodeit.core.application.user.port.in.dto.response;

public record UserAuthenticationResult(UserResult user, String passwordHash) {
}
