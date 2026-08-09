package com.sprint.mission.discodeit.dto.user;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record UserUpdateRequestDto(
        UUID userId,
        @Nullable
        UUID profileId, // BinaryContent 파일 그 자체가 들어와야한다.
        String newUsername,
        String newEmail,
        String newPassword,
        @Nullable
        byte[] data
) {

}
