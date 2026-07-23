package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@ToString
public class Channel {
    private String channelName;
    private List<User> members;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public Channel(String channelName, List<User> members) {
        this.channelName = channelName;
        this.members = members;

        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }


//    public void setChannel(String channelName, List<User> members) {
//        this.channelName = channelName;
//        this.updatedAt = System.currentTimeMillis();
//        System.out.println();
//    }
}
//TODO : 패키지 나눔 이제 안에 기능 뭐넣을지 생각해보기 어떻게 객체지향적이고

