package com.sprint.mission.discodeit.entity.dto.user;

import lombok.Value;

import java.util.UUID;

@Value
public class UserCreationDto {
    String name;
    String email;
    String password;
    UUID profileId;
}
