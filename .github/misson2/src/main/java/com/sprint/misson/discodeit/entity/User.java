package com.sprint.misson.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private Long updateAt;
    private String name;
    private String email;

    public User(String name, String email) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.name = name;
        this.email = email;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public void update(String updateName) {
        this.name = updateName;
        this.updateAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "이름 = " + this.name + " 메일 주소 = " + this.email;
    }
}
