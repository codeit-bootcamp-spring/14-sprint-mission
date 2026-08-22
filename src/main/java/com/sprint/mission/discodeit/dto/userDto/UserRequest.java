package com.sprint.mission.discodeit.dto.userDto;

import java.util.UUID;

public record UserRequest(
    UUID uuid,
    String name,
    String password,
    String email
) {

}
