package com.sprint.mission.discodeit.application.user.dto;

public record UserCreateRequest(
    String username,
    String email,
    String password
) {

}
