package com.sprint.mission.discodeit.user.web.dto.req;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequestDTO(
    @NotBlank(message = "입력 제대로요")
    String newUsername,

    @NotBlank(message = "입력 제대로요")
    @Email
    String newEmail,

    @NotBlank(message = "입력 제대로요")
    String newPassword
) {}
