package com.sprint.mission.discodeit.core.config.security;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class UserSessionKey {

    private final UUID userId;

    protected UserSessionKey(UUID userId) {
        this.userId = userId;
    }

    public static UserSessionKey by(UUID userId) {
        return new UserSessionKey(userId);
    }

    @Override
    public final boolean equals(Object other) {
        return other instanceof UserSessionKey key
                && Objects.equals(this.getUserId(), key.getUserId());
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(getUserId());
    }
}
