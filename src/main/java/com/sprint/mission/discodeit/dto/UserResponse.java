package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserResponse {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String username;
    private String email;
    private UUID profileImageId;
    private boolean isOnline;
    private Instant lastLoginAt;

    public UserResponse(User user, UserStatus userStatus) {
        this.id = user.getId();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImageId = user.getProfileId();
        this.isOnline = userStatus.isOnline();
        this.lastLoginAt = userStatus.getLastLoginAt();
    }
}
