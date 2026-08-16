package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private transient final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String name;
    private int age;

    public User (String name, int age){
        this.id = UUID.randomUUID();
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
        this.name = name;
        this.age = age;
    }

    public void update(String name, int age){
        this.name = name;
        this.age = age;
        this.updatedAt = System.currentTimeMillis();

    }
}