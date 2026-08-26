package com.sprint.mission.discodeit.dto.messagedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageUpdateRequestDto {
    // OpenAPI에서 요청을 new로 보내니까 dto의 필드도 new로 변경
    private String newContent;

    /* 현재 내 Service코드에서는 데이터를 getValues로 받는중
    서비스 코드를 원래 getNewContent로 바꿔줘야하지만 변경없이 가려고
    getValues()메서드 정의
     */
    public String getValues() {
        return newContent;
    }
}
