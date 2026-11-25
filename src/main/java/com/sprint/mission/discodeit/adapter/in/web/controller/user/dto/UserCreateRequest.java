package com.sprint.mission.discodeit.adapter.in.web.controller.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(@NotBlank @Size(max = 50) String username,
                                @NotBlank @Email @Size(max = 50) String email,
                                @NotBlank String password) {

}
