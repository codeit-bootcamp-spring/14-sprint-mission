package com.sprint.mission.discodeit.common.exception;

public record ErrorResponse(String detail) {
    public static ErrorResponse of(String detail) {
        return new ErrorResponse(detail);
    }
}
