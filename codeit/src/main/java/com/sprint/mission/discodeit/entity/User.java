package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    private final UUID id;
    private final Long createAt;
    private Long updateAt;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = null;
    }

    public void update(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password  = password;
        this.updateAt = System.currentTimeMillis();
    }
    public UUID getId(){
        return id;
    }

    public String getName() {
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getpassword(){return password;}
}
