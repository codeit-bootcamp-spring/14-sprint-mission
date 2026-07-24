package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class User implements Serializable {
    private final UUID userId = UUID.randomUUID();
    private final Long createAt = System.currentTimeMillis();
    private Long updateAt;
    @NonNull
    private String userName;
    private final Channel channel;

    public void updateName(String updateName){
        this.userName = updateName;
        this.updateAt = System.currentTimeMillis();
    }
}
