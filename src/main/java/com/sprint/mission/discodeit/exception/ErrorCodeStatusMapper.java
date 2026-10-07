package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorCodeStatusMapper {

    public HttpStatus toStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case USER_NOT_FOUND, CHANNEL_NOT_FOUND, MESSAGE_NOT_FOUND, READ_STATUS_NOT_FOUND, USER_STATUS_NOT_FOUND
            , BINARY_CONTENT_NOT_FOUND, RESOURCE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATE_USER, DUPLICATE_USER_STATUS -> HttpStatus.CONFLICT;
            case WRONG_PASSWORD, PRIVATE_CHANNEL_UPDATE,  INVALID_INPUT_VALUE -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
