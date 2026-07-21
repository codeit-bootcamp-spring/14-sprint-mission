package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class User {
    private final UUID userId = UUID.randomUUID();
    private final Long createAt = System.currentTimeMillis();
    private final Long updateAt = System.currentTimeMillis();
    private String userName;

    public void updateName(String updateName){
        this.userName = updateName;
    }
}
