package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Channel {
    private final UUID channelId = UUID.randomUUID();
    private final Long createAt = System.currentTimeMillis();
    private final Long updateAt = System.currentTimeMillis();
    private String channelName;

    public void updateName(String updateName){
        this.channelName = updateName;
    }
}
