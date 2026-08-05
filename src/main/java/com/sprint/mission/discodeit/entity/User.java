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

    // 사용자명, 나이, 이메일
    @Setter
    private String name;
    @Setter
    private int age;
    @Setter
    private String email;

    private Long createdAt;
    private Long updatedAt;

    // 생성자
    public User(String name, int age, String email) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.age = age;
        this.email = email;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    // update(name, age, email)
    public void update(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "User{id=" + this.id + ", name=" + this.name + ", age=" + this.age + ", email=" + this.email + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + "}";
    }
}
