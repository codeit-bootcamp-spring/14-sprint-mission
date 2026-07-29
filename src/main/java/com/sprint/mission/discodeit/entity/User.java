package com.sprint.mission.discodeit.entity;

public class User extends Common{
    private String userName;
    private String email;

    public User(String userName, String email) {
        super();
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void update(String userName, String email) {
        this.userName = userName;
        this.email = email;
        update();
    }

}
