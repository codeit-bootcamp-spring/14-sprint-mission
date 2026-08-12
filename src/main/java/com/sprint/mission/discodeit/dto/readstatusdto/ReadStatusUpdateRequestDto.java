package com.sprint.mission.discodeit.dto.readstatusdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatusUpdateRequestDto {
    private Instant lastReadAt;
}
