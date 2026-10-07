package com.sprint.mission.discodeit.user.application.dto;

public record UserCreateRequest(
    String username,
    String email,
    String password
) {

}
