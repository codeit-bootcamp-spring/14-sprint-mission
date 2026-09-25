package com.sprint.mission.discodeit.adaptor.dto.userDto;

public record UserCreateRequest(
    String username,
    String email,
    String password
) {

}
