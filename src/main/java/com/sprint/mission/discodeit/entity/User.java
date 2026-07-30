package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity{
    String name;
    String email;
    String nickname;

    private User(String name, String email, String nickname) {
        super();
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }

    public static User create(String name, String email, String nickname){
        return new User(name, email, nickname);
    }

    public void changeName(String name){
        this.name = name;
        newUpdatedAt();
    }

    public void changeEmail(String email){
        this.email = email;
        newUpdatedAt();
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
        newUpdatedAt();
    }
}
