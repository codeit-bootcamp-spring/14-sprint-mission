package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
   private final UUID id;
   private long createdAt;
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


    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }


    public void update(String name, int age){
       this.name = name;
       this.age = age;
       this.updatedAt = System.currentTimeMillis();

   }
}
