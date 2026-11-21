package com.sprint.mission.discodeit.user.security;

import com.sprint.mission.discodeit.user.domain.entity.Role;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record SecurityUserInfo(UUID userId, String username, Role role) {

    public static SecurityUserInfo init(UUID userId, String username, Role role) {
        return SecurityUserInfo.builder()
            .userId(userId)
            .username(username)
            .role(role)
            .build();
    }
}
