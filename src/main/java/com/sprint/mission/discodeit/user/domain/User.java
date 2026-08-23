package com.sprint.mission.discodeit.user.domain;

import com.sprint.mission.discodeit.common.entity.BaseEntity;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
public class User extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Nullable
    private UUID profileId;
    private String userName;
    private String email;
//    @Getter(AccessLevel.NONE)
    private String password;
    private UserStatus userStatus;

    private User(String userName, String email, String password) {
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public static User create(String userName, String email, String password){
        validateUserName(userName);
        validateEmail(email);
        validatePassword(password);
        return new User(userName, email, password);
    }

    public void update(String username, String email, String password){
        if(username != null) {
            validateUserName(username);
            this.userName = username;
        }
        if(email != null) {
            validateEmail(email);
            this.email = email;
        }
        if(password != null) {
            validatePassword(password);
            this.password = password;
        }
        this.updateUpdatedAt(Instant.now());
    }

    public void updateProfileId(UUID profileId){
        this.profileId = profileId;
    }

    public boolean checkPassword(String password){
        return Objects.equals(password, this.password);
    }

    private static void validateUserName(String userName){
        if(userName == null || userName.isBlank()){
            throw new IllegalArgumentException("userName은 비어 있을 수 없습니다.");
        }
    }

    private static void validateEmail(String email){
        if(email == null || email.isBlank() || !email.contains("@")){
            throw new IllegalArgumentException("올바른 email 형식이 아닙니다.");
        }
    }

    private static void validatePassword(String password){
        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("password는 비어있을 수 없습니다.");
        }
    }


}
