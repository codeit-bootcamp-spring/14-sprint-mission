package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Channel {
    private final UUID channelId = UUID.randomUUID();
    private final LocalDateTime createAt = LocalDateTime.now();
    private final LocalDateTime updateAt = LocalDateTime.now();
    private String channelName;

    public void updateName(String updateName){
        this.channelName = updateName;
    }
}
