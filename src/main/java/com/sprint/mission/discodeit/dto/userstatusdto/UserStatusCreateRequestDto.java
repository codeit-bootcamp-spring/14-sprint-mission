package com.sprint.mission.discodeit.dto.userstatusdto;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusCreateRequestDto {
    UUID userId;

    public UserStatus toEntity() {
        return new UserStatus(this.userId);
    }
}
