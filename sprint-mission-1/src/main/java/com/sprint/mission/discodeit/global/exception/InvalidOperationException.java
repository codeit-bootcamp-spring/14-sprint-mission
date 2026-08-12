package com.sprint.mission.discodeit.global.exception;

import java.util.UUID;

// 잘못 된 인수로 메소드 호출 실패
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }

    public static InvalidOperationException privateChannel(UUID id) {
        return new InvalidOperationException("비공개 채널은 수정할 수 없습니다: " + id);
    }
}
