package com.sprint.mission.discodeit.dto.readstatusdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatusUpdateRequestDto {
    // OpenAPI에서 요청을 new로 보내니까 dto의 필드도 new로 변경
    private Instant newLastReadAt;

    /* 현재 내 Service코드에서는 데이터를 getLastReadAt으로 받는중
    서비스 코드를 원래 getNewLastReadAt으로 바꿔줘야하지만 변경없이 가려고
    getLastReadAt()메서드 정의
     */
    public Instant getLastReadAt() {
        return newLastReadAt;
    }
}
