package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class User implements Serializable {
    // 직렬화
    private static final long serialVersionID = 1L;

    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    // 사용자명, 나이, 이메일
    @Setter
    private String name;
    @Setter
    private int age;
    @Setter
    private String email;

    // 생성자
    public User(String name, int age, String email) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    // update()
    public void update(String name, int age, String email) {
        this.updatedAt = System.currentTimeMillis();
        this.name = name;
        this.age = age;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{id=" + this.id + ", name=" + this.name + ", age=" + this.age + ", email=" + this.email + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + "}";
    }
}
