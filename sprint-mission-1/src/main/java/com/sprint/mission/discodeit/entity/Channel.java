package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Channel implements Serializable {
    private final UUID channelId = UUID.randomUUID();
    private final Long createAt = System.currentTimeMillis();
    private Long updateAt;
    @NonNull
    private String channelName;

    public void updateName(String updateName){
        this.channelName = updateName;
        this.updateAt = System.currentTimeMillis();
    }
}
