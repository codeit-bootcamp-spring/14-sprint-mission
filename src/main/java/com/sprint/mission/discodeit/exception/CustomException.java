package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ExceptionType type;

    public CustomException(ExceptionType type) {
        super(type.getDetail() + type.getDescription());    // 이건 뭐하는건지? QQQ
        this.type = type;
    }
}
