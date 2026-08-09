package com.sprint.mission.discodeit.dto.userstatusdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusUpdateRequestDto {
    private Instant lastActiveAt;
}
