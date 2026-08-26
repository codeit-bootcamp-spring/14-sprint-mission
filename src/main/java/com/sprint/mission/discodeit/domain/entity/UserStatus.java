package com.sprint.mission.discodeit.domain.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class UserStatus implements IdMapper{
    @Builder.Default
    UUID id = UUID.randomUUID();

    UUID userId;
    Instant lastActiveAt;

    @Builder.Default
    boolean online = false;
    @Builder.Default
    Instant createdAt = Instant.now();
    @Builder.Default
    Instant updatedAt = Instant.now();


    static public UserStatus init(UUID userId){
        return UserStatus.builder()
            .userId(userId).build();
    }

    public void login(){
        this.online = true;
        activateUser();
    }

    public void activateUser(){
        this.lastActiveAt = Instant.now();
        this.updatedAt = lastActiveAt;
    }

    public void activateUser(Instant activeAt){
        this.lastActiveAt = activeAt;
        this.updatedAt = Instant.now();
    }

    public boolean isActive(){
        if(!this.online) return false;

        Duration difference = Duration.between(this.lastActiveAt, Instant.now());
        return difference.toMinutes() < 5;
    }
}
