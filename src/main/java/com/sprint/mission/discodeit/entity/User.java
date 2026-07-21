package com.sprint.mission.discodeit.entity;

public class User extends BasicEntity {

    private String name;
    private String email;

    public User(String name, String email) {
        super();
        this.name = name;
        this.email = email;
    }


    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "User{id=" + id +
            ", username='" + name +
            "', email='" + email +
            "', creatAt='" + createdAt +
            "', updatedAt='" + updatedAt +
            "'}";
    }
}
