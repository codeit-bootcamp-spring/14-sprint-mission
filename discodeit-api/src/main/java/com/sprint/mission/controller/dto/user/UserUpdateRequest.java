package com.sprint.mission.controller.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @NotBlank
    String newUsername;

    @NotBlank
    @Email
    String newEmail;

    @NotBlank
    String newPassword;
}
