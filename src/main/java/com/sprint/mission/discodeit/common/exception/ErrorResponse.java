package com.sprint.mission.discodeit.common.exception;

import java.util.Map;

public record ErrorResponse(
        String code,
        String message
) {

    public static ErrorResponse from(ErrorCode errorCode){
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }
}
