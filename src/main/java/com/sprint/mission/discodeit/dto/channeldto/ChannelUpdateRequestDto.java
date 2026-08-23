package com.sprint.mission.discodeit.dto.channeldto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelUpdateRequestDto {
    // OpenAPI에서 요청을 new로 보내니까 dto의 필드도 new로 변경
    private String newName;
    private String newDescription;
    private List<UUID> memberIds;

    /* 현재 내 Service코드에서는 데이터를 getChannelName으로 받는중
    서비스 코드를 원래 getNewName으로 바꿔줘야하지만 변경없이 가려고
    getChannelName()메서드 정의
     */
    public String getChannelName() {
        return newName;
    }
}
