package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter //response dto에서 써야댐
@Setter
//유저의 마지막 활동시간을 관리하는 엔티티
public class UserStatus {
    private final UUID userId;
    private Instant lastActiveAt; //login으로 하고싶은데 나중에 login엔티티랑 헷갈릴듯

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    public UserStatus(UUID userId) {
        this.userId = userId;
        this.lastActiveAt = Instant.now();

        this.id=UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public void setLastActiveAt() {
        this.lastActiveAt = Instant.now();
        setUpdatedAt();
    }
}
