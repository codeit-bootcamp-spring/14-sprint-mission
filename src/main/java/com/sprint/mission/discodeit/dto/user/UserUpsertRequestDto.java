package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserUpsertRequestDto {

    @NotBlank
    String username;

    @Email
    @NotBlank
    String email;

    @NotBlank
    String password;

}
