package com.sprint.mission.discodeit.entity;

public class Channel extends Common {
    private static final long serialVersionUID = 1L;

    private ChannelType type;
    private String channelName;
    private String description;

    public Channel(ChannelType type, String channelName, String description) {
        super();
        this.type = type;
        this.channelName = channelName;
        this.description = description;
    }

    public ChannelType getType() {
        return type;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }

    public void update(ChannelType type, String channelName, String description) {
        this.type = type;
        this.channelName = channelName;
        this.description = description;
        update();
    }

    public String toString() {
        return String.format("채널종류: %s, 채널이름: %s, 설명: %s", type, channelName, description);
    }

}
