package com.sprint.mission.discodeit.dto.userstatusdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusUpdateRequestDto {
    // OpenAPI에서 요청을 new로 보내니까 dto의 필드도 new로 변경
    private Instant newLastActiveAt;

    /* 현재 내 Service코드에서는 데이터를 getLastActiveAt으로 받는중
    서비스 코드를 원래 getNewLastActiveAt으로 바꿔줘야하지만 변경없이 가려고
    getLastActiveAt()메서드 정의
     */
    public Instant getLastActiveAt() {
        return newLastActiveAt;
    }
}
