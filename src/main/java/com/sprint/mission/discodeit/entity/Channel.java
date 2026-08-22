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

    /**
     * type은 뺐다. PUBLIC/PRIVATE 전환은 참여자 유무 자체가 바뀌는 셈이라 의미가 깨진다.
     * null인 항목은 바꾸지 않는다 — User.update()와 같은 규칙.
     */
    public void update(String newChannelName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newChannelName != null && !newChannelName.equals(this.channelName)) {
            this.channelName = newChannelName;
            anyValueUpdated = true;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            update();
        }
    }

    public String toString() {
        return String.format("채널종류: %s, 채널이름: %s, 설명: %s", type, channelName, description);
    }

}
