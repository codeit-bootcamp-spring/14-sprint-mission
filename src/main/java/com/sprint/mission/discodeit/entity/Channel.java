package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity{

    private ChannelType channelType;
    private String memo;
    private String memoText;

    public Channel(ChannelType channelType, String memo, String memoText) {
        super();
        this.channelType = channelType;
        this.memo = memo;
        this.memoText = memoText;
    }

    public void update(ChannelType channelType, String memo, String memoText){
        this.channelType = channelType;
        this.memo = memo;
        this.memoText = memoText;
        this.updateUpdatedAt(System.currentTimeMillis());
    }

    public ChannelType getChannelType(){
        return channelType;
    }
    public String getMemo(){
        return memo;
    }
    public String getMemoText(){
        return memoText;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelType=" + channelType +
                ", memo='" + memo + '\'' +
                ", memoText='" + memoText + '\'' +
                '}';
    }
}
