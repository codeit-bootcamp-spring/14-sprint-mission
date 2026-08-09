package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.time.Instant;

@Getter
public class Channel extends BaseEntity{

    @Serial
    private static final long serialVersionUID = 1L;

    private ChannelType channelType;
    private String title;
    private String memo;

    public Channel(ChannelType channelType, String memo, String memoText) {
        super();
        this.channelType = channelType;
        this.title = memo;
        this.memo = memoText;
    }

    public void update(ChannelType channelType, String memo, String memoText){
        this.channelType = channelType;
        this.title = memo;
        this.memo = memoText;
        this.updateUpdatedAt(Instant.now());
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelType=" + channelType +
                ", title='" + title + '\'' +
                ", title='" + memo + '\'' +
                '}';
    }
}
