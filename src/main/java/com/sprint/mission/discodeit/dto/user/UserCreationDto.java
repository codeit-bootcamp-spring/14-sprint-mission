package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.util.UUID;

@Value
public class UserCreationDto {
    @NotBlank
    String name;
    @NotBlank
    String email;
    @NotBlank
    String password;
    UUID profileId;

    public User toUser() {
        return new User(name, email, password, profileId);
    }
}
