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

    // 채널명, 채널설명
    private String channelName;
    private String description;

    private Long createdAt;
    private Long updatedAt;

    // 생성자
    public Channel(String channelName, String description) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    // update(channelName)
    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        this.updatedAt = System.currentTimeMillis();
    }

    // update(description)
    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }

    // update(channelName, description)
    public void update(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Channel{id=" + this.id + ", channelName=" + this.channelName + ", description=" + this.description + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + "}";
    }
}
