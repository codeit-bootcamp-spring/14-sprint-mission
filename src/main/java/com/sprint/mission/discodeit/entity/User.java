package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity{

    private String userName;
    private String email;
    private String password;

    public User(String userName, String email, String password) {
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName(){
        return userName;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }

    public void update(String userName, String email,String password){
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.updateUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
