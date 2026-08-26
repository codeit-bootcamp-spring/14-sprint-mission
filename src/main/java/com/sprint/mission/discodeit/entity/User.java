package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class User extends Basic {
    private String name;
    private String password;
    private String email;
    private UUID profileId; //프로필사진 없어도 됨.

    public User(UUID id, String name, String password, String email) {
        super(id);
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public void update(String name, String password, String email) {
        if(name != null) {
            this.name = name;
        }
        if (password != null) {
            this.password = password;
        }
        if (email != null) {
            this.email = email;
        }
        super.updatedAt = Instant.now();
    }

    public void changeProfile(UUID profileId) {
        this.profileId = profileId; // null이면 프로필 제거
        this.updatedAt = Instant.now();
    }

    public String toString() {
        return String.format("아이디: %s, 생성일: %s, 수정일: %s, 이름: %s, 비밀번호: %s, 이메일: %s", super.id, super.createdAt, this.updatedAt, this.name, this.password, this.email);
    }
}
