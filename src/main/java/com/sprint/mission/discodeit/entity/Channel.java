package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    // 직렬화
    private static final long serialVersionID = 1L;

    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    // 채널명, 채널설명
    @Setter
    private String channelName;
    @Setter
    private String description;

    // 생성자
    public Channel(String channelName, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.channelName = channelName;
        this.description = description;
    }

    // update()
    public void update(String channelName, String description) {
        this.updatedAt = System.currentTimeMillis();
        this.channelName = channelName;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Channel{id=" + this.id + ", channelName=" + this.channelName + ", description=" + this.description + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + "}";
    }
}
