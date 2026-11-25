package com.sprint.mission.discodeit.adapter.in.web.controller.user.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(@Nullable @Size(max = 50) String newUsername,
                                @Nullable @Email @Size(max = 50) String newEmail,
                                @Nullable String newPassword) {

}
