package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserStatusCreateRequestDto {
    UUID userId;

    public static UserStatusCreateRequestDto from(UUID userId) {
        if (userId == null) {
            throw new CustomException(ExceptionType.USER_ID_IS_NULL);
        }
        return new UserStatusCreateRequestDto(userId);
    }
}
