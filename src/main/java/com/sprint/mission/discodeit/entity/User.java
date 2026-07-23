package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private final String email;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    //유저는 이름과 이메일을 가진다
    public User(String email, String name) {
        this.name = name;
        this.email = email;

        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public void setUpdatedAt () {
        this.updatedAt = System.currentTimeMillis();
    }

//    public void setUser(String name,String email) {
//        this.name = name;
//        this.email = email;
//        this.updatedAt = System.currentTimeMillis();
//        System.out.println("수정완료 이름 -> " + this.name + " 이메일 -> " + this.email);
//    }
//    @Override
//    public String toString() {
//
//    }
}
