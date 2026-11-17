package com.sprint.mission.discodeit.user.dto;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import jakarta.annotation.Nullable;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        ProfileResponse profile,
        String username,
        String email,
        boolean online
) {
    public static UserResponseDto from(User user, UserStatus userStatus){
        return new UserResponseDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getProfile() != null ? ProfileResponse.from(user.getProfile()) : null,
                user.getUserName(),
                user.getEmail(),
                userStatus.isOnline()
        );
    }

    public record ProfileResponse(UUID id, String fileName, Long size, String contentType){
        public static ProfileResponse from(BinaryContent binaryContent){
            if (binaryContent == null)
                return null;
            return new ProfileResponse(
                    binaryContent.getId(),
                    binaryContent.getFileName(),
                    binaryContent.getSize(),
                    binaryContent.getContentType()
            );
        }
    }
}
