package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;


public record UserCreateRequestDto(
    @NotBlank(message = "유저명은 필수입니다.")
    @Size(min = 2, max = 20, message = "유저명은 2~20자여야 합니다.")
    String username,

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 30, message = "비밀번호는 8~30자여야 합니다.")
    String password
) {


    public User toEntity(BinaryContent profileId){
        return new User(
            this.username,
            this.email,
            this.password,
            profileId
        );
    }
}
