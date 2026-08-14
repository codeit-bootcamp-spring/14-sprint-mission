package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User extends BaseEntity {
    // 사용자명, 이메일, 비밀번호
    @Setter
    private String username;
    @Setter
    private String email;
    @Setter
    private String password;

    // 생성자
    public User(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // update(name, age, email)
    public void update(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        super.updateTime();
    }

    @Override
    public String toString() {
        return "User{id=" + super.getId() + ", username=" + this.username + ", email=" + this.email + ", password=" + this.password + ", createdAt=" + super.getCreatedAt() + ", updatedAt=" + super.getUpdatedAt() + "}";
    }
}
