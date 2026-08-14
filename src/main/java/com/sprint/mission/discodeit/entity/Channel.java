package com.sprint.mission.discodeit.entity;

import lombok.Getter;

@Getter
public class Channel extends BaseEntity {
    // 채널타입, 채널명, 채널설명
    private ChannelType type;
    private String channelName;
    private String description;

    // 생성자
    public Channel(ChannelType type, String channelName, String description) {
        super();
        this.type = type;
        this.channelName = channelName;
        this.description = description;
    }

    // update(channelName)
    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        super.updateTime();
    }

    // update(description)
    public void updateDescription(String description) {
        this.description = description;
        super.updateTime();
    }

    // update(channelName, description)
    public void update(ChannelType type, String channelName, String description) {
        this.type = type;
        this.channelName = channelName;
        this.description = description;
        super.updateTime();
    }

    @Override
    public String toString() {
        return "Channel{id=" + super.getId() + ", type=" + this.type + ", channelName=" + this.channelName + ", description=" + this.description + ", createdAt=" + super.getCreatedAt() + ", updatedAt=" + super.getUpdatedAt() + "}";
    }
}
