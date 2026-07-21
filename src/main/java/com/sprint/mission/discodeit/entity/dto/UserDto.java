package com.sprint.mission.discodeit.entity.dto;

import lombok.Value;

@Value
public class UserDto {
    String name;

    public static UserDto of(String name) {
        return new UserDto(name);
    }
}
