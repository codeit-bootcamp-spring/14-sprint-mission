package com.sprint.misson.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private final UUID authorId;
    private final UUID channelId;
    private Long updatedAt;
    private String msg;
    private String author;
    private String channelName;


    public Message(String msg, User user, Channel channel) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.msg = msg;
        this.author = user.getName();
        this.channelName = channel.getChannelName();
        this.authorId = user.getId();
        this.channelId = channel.getId();
    }

    public UUID getId() {
        return this.id;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public UUID getAuthorId() {
        return this.authorId;
    }

    public UUID getChannelId() {
        return this.channelId;
    }

    public void update(String updateMsg) {
        this.msg = updateMsg;
    }

    @Override
    public String toString() {
        return "내용 : " + this.msg + " 글쓴이 : " + this.author + " 채널 : " +this.channelName;
    }

}
