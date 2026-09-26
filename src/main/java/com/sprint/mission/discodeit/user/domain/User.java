package com.sprint.mission.discodeit.user.domain;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.common.entity.BaseUpdatableEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.time.Instant;
import java.util.Objects;

@Getter
@Entity
@Table(name = "users")
@ToString
@NoArgsConstructor
public class User extends BaseUpdatableEntity {

    @Column(name = "username", unique = true, nullable = false)
    private String userName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;
//    @Getter(AccessLevel.NONE)

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id", unique = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private BinaryContent profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus userStatus;

    private User(String userName, String email, String password) {
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public void updateUserStatus(UserStatus userStatus){
        this.userStatus = userStatus;
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

    public void updateProfile(BinaryContent profile){
        this.profile = profile;
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
