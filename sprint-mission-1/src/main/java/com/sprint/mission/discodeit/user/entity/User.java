package com.sprint.mission.discodeit.user.entity;

import jakarta.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID userId = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    private String password;
    private Instant updatedAt;
    @NonNull
    private String userName;
    private String email;
    @Nullable
    private UUID binaryId;

    public User(String userName, String password, String email, UUID binaryId) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.binaryId = binaryId;
    }

    public void updateName(String updateName) {
        this.userName = updateName;
        this.updatedAt = Instant.now();
    }

    public void updateEmail(String email) {
        this.email = email;
        this.updatedAt = Instant.now();
    }

    public void updatePassword(String password) {
        this.password = password;
        this.updatedAt = Instant.now();
    }

    public void updateBinaryId(UUID binaryId) {
        this.binaryId = binaryId;
        this.updatedAt = Instant.now();
    }
}
