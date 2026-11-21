package com.sprint.mission.discodeit.user.web.dto.req;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// 업서트?
public record UserCreateRequestDTO(
    @NotBlank
    @Email
    String email,

    @NotBlank
    String password,

    @NotBlank
    String username
) {
}