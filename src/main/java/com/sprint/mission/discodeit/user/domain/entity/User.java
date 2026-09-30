
package com.sprint.mission.discodeit.user.domain.entity;

import com.sprint.mission.discodeit.baseentity.BaseUpdatableEntity;
import com.sprint.mission.discodeit.binarycontent.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;

@ToString
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

    //필수 입력 란
    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "username", nullable = false)
    String name;

    @Column(name = "password", nullable = false)
    String userPassword;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    BinaryContent profileImage;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    UserStatus userStatus;          // 양방향 관계로 둠

    public static User init(String email, String userPassword, String name, BinaryContent profileImage){
        return User.builder()
            .email(email)
            .userPassword(userPassword)
            .name(name)
            .profileImage(profileImage)
            .build();
    }

    public void updateName(String name){
        this.name = name;
    }

    public void updateAllField(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.userPassword = password;
    }

    //프로필 이미지 업데이트 시
    public void updateProfileImage(BinaryContent profileImage){
        this.profileImage = profileImage;
    }

    public boolean hasProfileImage(){
        return Objects.nonNull(this.profileImage);
    }

    public void verifyPassword(PasswordEncoder passwordEncoder, String password){
        if(!passwordEncoder.matches(password, this.userPassword)){
            throw new CustomException(CustomErrorCode.USER_AUTH_MISMATCH);
        }
    }

    public void assignUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}
