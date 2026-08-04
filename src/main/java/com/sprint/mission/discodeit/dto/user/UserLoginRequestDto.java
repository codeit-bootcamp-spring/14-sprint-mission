package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class UserLoginRequestDto {
    @NotBlank
    String name;
    @NotBlank
    String password;
}
