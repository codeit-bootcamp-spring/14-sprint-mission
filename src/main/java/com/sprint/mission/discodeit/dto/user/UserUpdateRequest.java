package com.sprint.mission.discodeit.dto.user;

import jakarta.annotation.Nullable;

public record UserUpdateRequest(@Nullable String newUsername,
                                @Nullable String newEmail,
                                @Nullable String newPassword) {

}
