package com.sprint.mission.discodeit.user.dto;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record UserUpdateRequestDto(
        @Nullable
        String newUsername,
        String newEmail,
        String newPassword) {

}
