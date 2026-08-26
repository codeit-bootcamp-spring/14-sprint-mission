
package com.sprint.mission.discodeit.domain.entity;

import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
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
public class User implements Serializable, IdMapper {
    private static final long serialVersionUID = 1L;

    @Builder.Default
    UUID id = UUID.randomUUID();        //객체 생성 시 자동 할당

    //필수 입력 란
    String email;
    String userPassword;
    String name;

    @Builder.Default
    UUID profileId = null;      //todo : 추후 수정
    @Builder.Default
    Instant createdAt = Instant.now();
    @Builder.Default
    Instant updatedAt = Instant.now();


    public static User init(String email, String userPassword, String name){
        return User.builder().
            email(email).userPassword(userPassword).name(name).build();
    }

    public void updateName(String name){
        this.name = name;
        updatedAt = Instant.now();
    }

    public void updateAllField(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.userPassword = password;
        updatedAt = Instant.now();
    }

    //프로필 이미지 업데이트 시
    public void updateProfileImage(UUID profileId){
        this.profileId = profileId;
        updatedAt = Instant.now();
    }

    public boolean hasProfileImage(){
        return Objects.nonNull(this.profileId);
    }

    public void verifyPassword(PasswordEncoder passwordEncoder, String password){
        if(!passwordEncoder.matches(password, this.userPassword)){
            throw new CustomException(CustomErrorCode.USER_AUTH_MISMATCH);
        }
    }
}
