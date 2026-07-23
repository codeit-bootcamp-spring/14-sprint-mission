package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import lombok.Getter;

import java.io.Serializable;

public class User extends Entity implements Serializable {
    @Getter
    private String username;
    @Getter
    private String email;
    private String password;    // Getter 막기

    // UserRequestDto 통해서 User 객체를 생성할것이기 때문에 private으로
    private User(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // 이 정적 메소드를 통해서 User 객체 생성 - 딱히 사용할 필요 없는지? QQQ
    public static User from(UserCreateRequestDto requestDto) {
        return new User(
                requestDto.getUsername(),
                requestDto.getEmail(),
                requestDto.getPassword()
        );
    }

    public void update(String username, String email, String password) {
        boolean isUpdated = false;

        // username, email, password 중 하나만 바뀌어도 updatedAt timestamp가 바뀐다
        if (username != null && !username.equals(this.username)) {
            isUpdated = true;
            this.username = username;
        }
        if (email != null && !email.equals(this.email)) {
            isUpdated = true;
            this.email = email;
        }
        if (password != null && !password.equals(this.password)) {
            isUpdated = true;
            this.password = password;
        }

        if (isUpdated) {
            updateTimestamp();
        }
    }
}
