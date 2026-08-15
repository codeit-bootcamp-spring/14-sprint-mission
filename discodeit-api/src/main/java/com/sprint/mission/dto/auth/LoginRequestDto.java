package com.sprint.mission.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequestDto {

    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

}
