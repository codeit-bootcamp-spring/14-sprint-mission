package com.sprint.mission.discodeit.entity.dto.user;

import com.sprint.mission.discodeit.entity.User;
import lombok.Value;

import java.util.UUID;

@Value
public class UserCreationDto {
    String name;
    String email;
    String password;
    UUID profileId;

    public User toUser() {
        return new User(name, email, password, profileId);
    }
}
