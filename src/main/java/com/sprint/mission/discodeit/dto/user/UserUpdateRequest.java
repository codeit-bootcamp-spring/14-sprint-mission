package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(@NotNull String newUsername,
                                @NotNull String newEmail,
                                @NotNull String newPassword) {

}
